package com.mealam.showdown.battle;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BattleService {

	private static final int MAX_PLAYERS = 2;

	private final BattleRepository repo = new BattleRepository();
	private final PartyService partyService = new PartyService();
	private final Map<BattleId, TurnManager> turnManagers = new ConcurrentHashMap<>();

	public BattleContext createBattle(CreateBattleRequest pRequest) {
		List<UserId> playerIds = null;
		List<UserId> spectatorIds = null;

		if (pRequest != null && pRequest.playerIds() != null) {
			playerIds = pRequest.playerIds().stream()
					.map(UserId::parse)
					.collect(Collectors.toList());
		}

		if (pRequest != null && pRequest.spectatorIds() != null) {
			spectatorIds = pRequest.spectatorIds().stream()
					.map(UserId::parse)
					.collect(Collectors.toList());
		}

		BattleId battleId = BattleId.generate();
		TurnManager turnManager = new TurnManager();
		TurnContext initialTurn = turnManager.createBattle();

		turnManagers.put(battleId, turnManager);

		var battle = new BattleContext(
				battleId,
				playerIds,
				spectatorIds,
				initialTurn,
				Phase.REGISTRY.defaultVersion(),
				null
		);

		repo.save(battle);
		return battle;
	}

	public BattleContext startBattle(BattleId pId) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		TurnManager turnManager = turnManagers.get(pId);
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
				battle.battleId(),
				battle.playerIds(),
				battle.spectatorIds(),
				startContext,
				battle.phase(),
				battle.winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}

	public BattleContext advanceTurn(BattleId pId, TurnContext pTurnData) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		if (battle.turnContext() == null || battle.turnContext().turnNumber() == TurnManager.NOT_STARTED) {
			throw new IllegalStateException("Battle not started");
		}

		if (battle.turnContext().turnNumber() == TurnManager.FINISHED) {
			throw new IllegalStateException("Battle already finished");
		}

		TurnManager turnManager = turnManagers.get(pId);
		if (turnManager == null) {
			throw new IllegalStateException("Battle already finished");
		}

		TurnContext input = (pTurnData != null) ? pTurnData
				: new TurnContext(battle.turnContext().turnNumber() + 1, null);

		TurnContext newTurn = turnManager.advance(input);

		var updated = new BattleContext(
				battle.battleId(),
				battle.playerIds(),
				battle.spectatorIds(),
				newTurn,
				battle.phase(),
				battle.winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}

	public BattleContext finishBattle(BattleId pId, UserId pWinnerId) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		TurnManager turnManager = turnManagers.get(pId);
		if (turnManager != null) {
			turnManager.finish();
		}

		var updated = new BattleContext(
				battle.battleId(),
				battle.playerIds(),
				battle.spectatorIds(),
				new TurnContext(TurnManager.FINISHED, null),
				battle.phase(),
				pWinnerId
		);

		repo.update(updated);
		turnManagers.remove(pId);
		return updated;
	}

	public BattleContext getBattle(BattleId pId) {
		return repo.get(pId);
	}

	public JoinBattleResponse joinBattle(BattleId pId, JoinBattleRequest pRequest) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		UserId user = UserId.parse(pRequest.userId());

		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		UserProfileContext profileContext = new UserProfileContext(new UserContext(user, user.toString() /* placeholder */)); // TODO: Make a system to fetch user profiles from the ID
		if (players.contains(user)) {
			List<DragonBattleContext> party = partyService.getUserParty(user);
			party = partyService.preparePartyForBattle(party);
			var playerCtx = new PlayerBattleContext(profileContext, party, false);
			return new JoinBattleResponse.Player(playerCtx);
		}

		if (players.size() < MAX_PLAYERS) {
			players.add(user);
			spectators.remove(user);
		} else {
			if (!spectators.contains(user)) spectators.add(user);
			players.remove(user);
		}

		var updated = new BattleContext(
				battle.battleId(),
				players,
				spectators,
				battle.turnContext(),
				battle.phase(),
				battle.winnerPlayerId()
		);

		repo.update(updated);

		if (players.contains(user)) {
			List<DragonBattleContext> party = partyService.getUserParty(user);
			party = partyService.preparePartyForBattle(party);
			var playerCtx = new PlayerBattleContext(profileContext, party, false);
			return new JoinBattleResponse.Player(playerCtx);
		} else {
			var spectatorCtx = new SpectatorBattleContext(profileContext);
			return new JoinBattleResponse.Spectator(spectatorCtx);
		}
	}

	public BattleContext leaveBattle(BattleId pId, LeaveBattleRequest pRequest) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		UserId user = UserId.parse(pRequest.userId());

		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		boolean removed = players.remove(user) | spectators.remove(user);

		if (!removed) return battle;

		var updated = new BattleContext(
				battle.battleId(),
				players,
				spectators,
				battle.turnContext(),
				battle.phase(),
				battle.winnerPlayerId()
		);

		repo.update(updated);
		return updated;
	}
}