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
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		UserId user = UserId.parse(pRequest.userId());
		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		UserProfileContext profileContext = new UserProfileContext(new UserContext(user, user.toString()));
		if (players.contains(user)) {
			List<DragonBattleContext> party = partyService.getUserParty(user);
			party = partyService.preparePartyForBattle(party);
			var playerCtx = new PlayerBattleContext(profileContext, party, false);
			return new JoinBattleResponse.Player(pBattleId, playerCtx);
		}

		if (players.size() < MAX_PLAYERS) {
			players.add(user);
			spectators.remove(user);
		} else {
			if (!spectators.contains(user)) spectators.add(user);
			players.remove(user);
		}

		var updated = new BattleContext(
				battle.battleId(), players, spectators,
				battle.turnContext(), battle.phase(), battle.winnerPlayerId()
		);

		repo.update(updated);

		if (players.contains(user)) {
			List<DragonBattleContext> party = partyService.getUserParty(user);
			party = partyService.preparePartyForBattle(party);
			var playerCtx = new PlayerBattleContext(profileContext, party, false);
			return new JoinBattleResponse.Player(pBattleId, playerCtx);
		} else {
			var spectatorCtx = new SpectatorBattleContext(profileContext);
			return new JoinBattleResponse.Spectator(pBattleId, spectatorCtx);
		}
	}

	@Override
	public BattleContext leaveBattle(BattleId pBattleId, LeaveBattleRequest pRequest) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		UserId user = UserId.parse(pRequest.userId());
		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		boolean removed = players.remove(user) | spectators.remove(user);
		if (!removed) return battle;

		var updated = new BattleContext(
				battle.battleId(), players, spectators,
				battle.turnContext(), battle.phase(), battle.winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}
}