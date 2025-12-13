package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.context.DragonBattleContext;
import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.context.SpectatorBattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
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
import java.util.stream.Collectors;

public class BattleService {

	private static final int MAX_PLAYERS = 2;

	private final BattleRepository repo = new BattleRepository();
	private final PartyService partyService = new PartyService();

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