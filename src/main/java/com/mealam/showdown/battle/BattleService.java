package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.user.data.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleService {

	private static final int MAX_PLAYERS = 2;

	private final BattleRepository repo = new BattleRepository();

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

		var battle = new BattleContext(
				BattleId.generate(),
				playerIds,
				spectatorIds,
				null,
				Phase.REGISTRY.defaultVersion(),
				null
		);

		repo.save(battle);
		return battle;
	}

	public BattleContext getBattle(BattleId pId) {
		return repo.get(pId);
	}

	public void applyStateChange(BattleContext pBattle) {
		repo.update(pBattle);
	}

	public BattleContext joinBattle(BattleId pId, JoinBattleRequest pRequest) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		UserId user = UserId.parse(pRequest.userId());

		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		if (players.contains(user)) {
			return battle;
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
		return updated;
	}

	public BattleContext leaveBattle(BattleId pId, LeaveBattleRequest pRequest) {
		var battle = repo.get(pId);
		if (battle == null) return null;

		UserId user = UserId.parse(pRequest.userId());

		// build mutable copies
		List<UserId> players = new ArrayList<>(battle.playerIds() != null ? battle.playerIds() : List.of());
		List<UserId> spectators = new ArrayList<>(battle.spectatorIds() != null ? battle.spectatorIds() : List.of());

		boolean removed = players.remove(user) | spectators.remove(user);

		// if nothing removed, return current battle (no-op)
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