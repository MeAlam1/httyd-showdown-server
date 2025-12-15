package com.mealam.showdown.battle.domain;

import com.mealam.showdown.battle.api.BattleRepository;
import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.context.*;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.TurnManager;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.request.TurnBattleRequest;
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
				initialTurn, Phase.REGISTRY.defaultVersion(), null,
				new ArrayList<>()
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

		UserId startingPlayer = battle.playerIds().getFirst();
		TurnContext startContext = turnManager.startBattle(startingPlayer);

		var inProgress = Phase.REGISTRY.versions().get("in_progress");
		List<TurnContext> history = new ArrayList<>(battle.turnHistory());

		var updated = new BattleContext(
				battle.battleId(), battle.playerIds(), battle.spectatorIds(),
				startContext, inProgress, battle.winnerPlayerId(),
				history
		);

		repo.update(updated);
		return updated;
	}

	@Override
	public BattleContext advanceTurn(BattleId pBattleId, UserId pActingUserId, TurnBattleRequest pTurnData) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		if (battle.turnContext() == null || battle.turnContext().turnNumber() == TurnManager.NOT_STARTED) {
			throw new IllegalStateException("Battle not started");
		}
		if (battle.turnContext().turnNumber() == TurnManager.FINISHED) {
			throw new IllegalStateException("Battle already finished");
		}
		if (pActingUserId == null) {
			throw new IllegalArgumentException("Acting user is required");
		}
		if (battle.playerIds() == null || !battle.playerIds().contains(pActingUserId)) {
			throw new IllegalArgumentException("Acting user is not a player in this battle");
		}

		UserId active = battle.turnContext().activePlayerId();
		if (active == null || !active.equals(pActingUserId)) {
			throw new IllegalArgumentException("Only the active player can act");
		}

		TurnManager turnManager = turnManagers.get(pBattleId);
		if (turnManager == null) {
			throw new IllegalStateException("Battle already finished");
		}

		Map<UserId, String> merged = new LinkedHashMap<>();
		if (battle.turnContext().actions() != null) merged.putAll(battle.turnContext().actions());
		if (pTurnData != null && pTurnData.action() != null && !pTurnData.action().isBlank()) {
			merged.put(pActingUserId, pTurnData.action());
		}

		boolean bothActed = hasAllPlayersActed(battle.playerIds(), merged);

		UserId currentActive = battle.turnContext().activePlayerId();
		UserId nextActive;
		if (bothActed) {
			nextActive = deriveNextActivePlayer(battle.playerIds(), currentActive);
		} else {
			nextActive = playerWhoHasNotActed(battle.playerIds(), merged, currentActive);
		}

		List<TurnContext> history = new ArrayList<>(battle.turnHistory());
		BattleContext updated;

		if (bothActed) {
			TurnContext newTurnHeader = turnManager.advance(nextActive);
			TurnContext newTurn = new TurnContext(newTurnHeader.turnNumber(), null, newTurnHeader.activePlayerId());

			TurnContext resolvedPrevTurn = new TurnContext(
					battle.turnContext().turnNumber(),
					merged,
					battle.turnContext().activePlayerId()
			);
			history.add(resolvedPrevTurn);

			updated = new BattleContext(
					battle.battleId(), battle.playerIds(), battle.spectatorIds(),
					newTurn, battle.phase(), battle.winnerPlayerId(),
					history
			);
		} else {
			TurnContext sameTurn = new TurnContext(battle.turnContext().turnNumber(), merged, nextActive);
			history.add(sameTurn);
			updated = new BattleContext(
					battle.battleId(), battle.playerIds(), battle.spectatorIds(),
					sameTurn, battle.phase(), battle.winnerPlayerId(),
					history
			);
		}

		repo.update(updated);
		return updated;
	}

	private boolean hasAllPlayersActed(List<UserId> pPlayers, Map<UserId, String> pActions) {
		if (pPlayers == null || pPlayers.size() < MAX_PLAYERS) return false;
		if (pActions == null || pActions.isEmpty()) return false;
		for (UserId p : pPlayers) {
			if (!pActions.containsKey(p)) return false;
		}
		return true;
	}

	private UserId playerWhoHasNotActed(List<UserId> pPlayers, Map<UserId, String> pActions, UserId pCurrentActive) {
		if (pPlayers == null || pPlayers.isEmpty()) return null;
		for (UserId p : pPlayers) {
			if (pActions == null || !pActions.containsKey(p)) {
				return p;
			}
		}
		return pCurrentActive != null ? pCurrentActive : pPlayers.getFirst();
	}

	private UserId deriveNextActivePlayer(List<UserId> pPlayers, UserId pCurrent) {
		if (pPlayers == null || pPlayers.isEmpty()) return null;
		if (pCurrent == null) return pPlayers.getFirst();
		int idx = pPlayers.indexOf(pCurrent);
		if (idx < 0) return pPlayers.getFirst();
		return pPlayers.get((idx + 1) % pPlayers.size());
	}

	@Override
	public BattleContext finishBattle(BattleId pBattleId, UserId pWinnerId) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		TurnManager tm = turnManagers.get(pBattleId);
		if (tm != null) tm.finish();

		TurnContext finishedTurn = new TurnContext(TurnManager.FINISHED, null, null);
		List<TurnContext> history = new ArrayList<>(battle.turnHistory());
		history.add(finishedTurn);

		var updated = new BattleContext(
				battle.battleId(), battle.playerIds(), battle.spectatorIds(),
				finishedTurn, battle.phase(), pWinnerId,
				history
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
				ctx.battle().turnContext(), ctx.battle().phase(), ctx.battle().winnerPlayerId(),
				ctx.battle().turnHistory()
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
				ctx.battle().turnContext(), ctx.battle().phase(), ctx.battle().winnerPlayerId(),
				ctx.battle().turnHistory()
		);

		repo.update(updated);
		return updated;
	}

	private JoinBattleResponse.Player buildPlayerJoinResponse(BattleId pBattleId, UserProfileContext pContext, UserId pUserId) {
		List<DragonBattleContext> party = partyService.getUserParty(pUserId);
		party = partyService.preparePartyForBattle(party);
		var playerCtx = new PlayerBattleContext(pContext, party);
		return new JoinBattleResponse.Player(pBattleId, playerCtx);
	}

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