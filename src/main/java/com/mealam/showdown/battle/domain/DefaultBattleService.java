package com.mealam.showdown.battle.domain;

import com.mealam.showdown.battle.api.BattleRepository;
import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.context.*;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.turns.TurnContext;
import com.mealam.showdown.battle.data.turns.TurnManager;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import com.mealam.showdown.battle.party.PartyService;
import com.mealam.showdown.user.context.UserContext;
import com.mealam.showdown.user.context.UserProfileContext;
import com.mealam.showdown.user.data.UserId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultBattleService implements BattleService {

	private static final int MAX_PLAYERS = 2;

	private final BattleRepository repo;
	private final PartyService partyService;
	private final Map<BattleId, TurnManager> turnManagers = new ConcurrentHashMap<>();

	public DefaultBattleService(BattleRepository pRepository, PartyService pPartyService) {
		this.repo = Objects.requireNonNull(pRepository);
		this.partyService = Objects.requireNonNull(pPartyService);
	}

	@Override
	public BattleContext createBattle(CreateBattleRequest pRequest) {
		List<UserId> playerIds = null;
		List<UserId> spectatorIds = null;

		if (pRequest != null && pRequest.playerIds() != null) {
			playerIds = pRequest.playerIds().stream().map(UserId::parse).collect(Collectors.toList());
		}
		if (pRequest != null && pRequest.spectatorIds() != null) {
			spectatorIds = pRequest.spectatorIds().stream().map(UserId::parse).collect(Collectors.toList());
		}

		BattleId battleId = BattleId.generate();
		TurnManager turnManager = new TurnManager();
		TurnContext initialTurn = turnManager.createBattle();

		turnManagers.put(battleId, turnManager);

		var battle = new BattleContext(
				battleId, playerIds, spectatorIds,
				initialTurn, Phase.REGISTRY.defaultVersion(), null
		);

		repo.save(battle);
		return battle;
	}

	@Override
	public BattleContext startBattle(BattleId pBattleId) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		TurnManager turnManager = turnManagers.get(pBattleId);
		if (turnManager == null) {
			throw new IllegalStateException("Turn manager not found for battle");
		}

		if (battle.turnContext() != null && battle.turnContext().turnNumber() != TurnManager.NOT_STARTED) {
			throw new IllegalStateException("Battle already started");
		}
		if (battle.playerIds() == null || battle.playerIds().isEmpty()) {
			throw new IllegalStateException("Cannot start battle: no players have joined");
		}
		if (battle.playerIds().size() < MAX_PLAYERS) {
			throw new IllegalStateException("Cannot start battle: need 2 players, but only " + battle.playerIds().size() + " joined");
		}

		TurnContext startContext = turnManager.startBattle();

		var updated = new BattleContext(
				battle.battleId(), battle.playerIds(), battle.spectatorIds(),
				startContext, battle.phase(), battle.winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}

	@Override
	public BattleContext advanceTurn(BattleId pBattleId, TurnContext pTurnData) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		if (battle.turnContext() == null || battle.turnContext().turnNumber() == TurnManager.NOT_STARTED) {
			throw new IllegalStateException("Battle not started");
		}
		if (battle.turnContext().turnNumber() == TurnManager.FINISHED) {
			throw new IllegalStateException("Battle already finished");
		}

		TurnManager turnManager = turnManagers.get(pBattleId);
		if (turnManager == null) {
			throw new IllegalStateException("Battle already finished");
		}

		TurnContext input = (pTurnData != null)
				? pTurnData
				: new TurnContext(battle.turnContext().turnNumber() + 1, null);

		TurnContext newTurn = turnManager.advance(input);

		var updated = new BattleContext(
				battle.battleId(), battle.playerIds(), battle.spectatorIds(),
				newTurn, battle.phase(), battle.winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}

	@Override
	public BattleContext finishBattle(BattleId pBattleId, UserId pWinnerId) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		TurnManager tm = turnManagers.get(pBattleId);
		if (tm != null) tm.finish();

		var updated = new BattleContext(
				battle.battleId(), battle.playerIds(), battle.spectatorIds(),
				new TurnContext(TurnManager.FINISHED, null), battle.phase(), pWinnerId
		);

		repo.update(updated);
		turnManagers.remove(pBattleId);
		return updated;
	}

	@Override
	public BattleContext getBattle(BattleId pBattleId) {
		return repo.get(pBattleId);
	}

	@Override
	public JoinBattleResponse joinBattle(BattleId pBattleId, JoinBattleRequest pRequest) {
		var ctx = resolveBattleUserLists(pBattleId, pRequest.userId());
		if (ctx == null) return null;

		UserProfileContext profileContext = new UserProfileContext(new UserContext(ctx.userId(), ctx.userId().toString()));
		if (ctx.players().contains(ctx.userId())) {
			return buildPlayerJoinResponse(pBattleId, profileContext, ctx.userId());
		}

		if (ctx.players().size() < MAX_PLAYERS) {
			ctx.players().add(ctx.userId());
			ctx.spectators().remove(ctx.userId());
		} else {
			if (!ctx.spectators().contains(ctx.userId())) ctx.spectators().add(ctx.userId());
			ctx.players().remove(ctx.userId());
		}

		var updated = new BattleContext(
				ctx.battle().battleId(), ctx.players(), ctx.spectators(),
				ctx.battle().turnContext(), ctx.battle().phase(), ctx.battle().winnerPlayerId()
		);

		repo.update(updated);

		if (ctx.players().contains(ctx.userId())) {
			return buildPlayerJoinResponse(pBattleId, profileContext, ctx.userId());
		} else {
			var spectatorCtx = new SpectatorBattleContext(profileContext);
			return new JoinBattleResponse.Spectator(pBattleId, spectatorCtx);
		}
	}

	@Override
	public BattleContext leaveBattle(BattleId pBattleId, LeaveBattleRequest pRequest) {
		var ctx = resolveBattleUserLists(pBattleId, pRequest.userId());
		if (ctx == null) return null;

		boolean removed = ctx.players().remove(ctx.userId()) | ctx.spectators().remove(ctx.userId());
		if (!removed) return ctx.battle();

		var updated = new BattleContext(
				ctx.battle().battleId(), ctx.players(), ctx.spectators(),
				ctx.battle().turnContext(), ctx.battle().phase(), ctx.battle().winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}

	private JoinBattleResponse.Player buildPlayerJoinResponse(BattleId pBattleId, UserProfileContext pContext, UserId pUserId) {
		List<DragonBattleContext> party = partyService.getUserParty(pUserId);
		party = partyService.preparePartyForBattle(party);
		var playerCtx = new PlayerBattleContext(pContext, party, false);
		return new JoinBattleResponse.Player(pBattleId, playerCtx);
	}

	// TODO: Look into Refactoring this logic to a separate class if it gets more widely used

	private BattleUserLists resolveBattleUserLists(BattleId pBattleId, String rawUserId) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		if (rawUserId == null || rawUserId.isBlank()) {
			throw new IllegalArgumentException("userId is required");
		}

		UserId user = UserId.parse(rawUserId);
		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		return new BattleUserLists(battle, user, players, spectators);
	}

	private record BattleUserLists(BattleContext battle, UserId userId, List<UserId> players, List<UserId> spectators) {
	}
}