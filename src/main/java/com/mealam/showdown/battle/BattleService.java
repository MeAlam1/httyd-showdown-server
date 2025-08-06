/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.context.SpectatorBattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.turns.Turns;
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import com.mealam.showdown.user.context.UserProfileContext;
import com.mealam.showdown.user.context.UserProfileContextService;
import com.mealam.showdown.user.data.UserId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class BattleService {

	private final Map<BattleId, BattleContext> battles = new ConcurrentHashMap<>();
	private final Map<BattleId, Map<UserId, PlayerBattleContext>> battlePlayers = new ConcurrentHashMap<>();
	private final Map<BattleId, Map<UserId, SpectatorBattleContext>> battleSpectators = new ConcurrentHashMap<>();
	private final UserProfileContextService userProfileContextService;

	public BattleService(UserProfileContextService pService) {
		this.userProfileContextService = pService;
	}

	public CreateBattleResponse createBattle(String pPlayerId) {
		BattleId battleId = BattleId.generate();
		UserId creatorId = UserId.parse(pPlayerId);
		BattleContext context = new BattleContext(
				battleId,
				List.of(creatorId),
				List.of(),
				new Turns().createBattle(),
				Phase.REGISTRY.defaultVersion(),
				null);
		battles.put(battleId, context);

		UserProfileContext userProfileContext = userProfileContextService.getByUserId(creatorId);
		PlayerBattleContext playerContext = new PlayerBattleContext(userProfileContext, List.of(), false);
		battlePlayers.put(battleId, new ConcurrentHashMap<>(Map.of(creatorId, playerContext)));

		return new CreateBattleResponse(battleId.value(), context);
	}

	public BattleContext getBattle(String pBattleId) {
		BattleId id = BattleId.parse(pBattleId);
		return battles.getOrDefault(id, null);
	}

	public JoinBattleResponse joinBattle(String pBattleId, String pUserId) {
		BattleId id = BattleId.parse(pBattleId);
		UserId user = UserId.parse(pUserId);

		BattleContext context = battles.get(id);
		if (context == null) {
			throw new NoSuchElementException("Battle not found");
		}

		if (context.playerIds().contains(user)) {
			PlayerBattleContext playerContext = battlePlayers.getOrDefault(id, Map.of()).get(user);
			if (playerContext == null) {
				UserProfileContext userProfileContext = userProfileContextService.getByUserId(user);
				playerContext = new PlayerBattleContext(userProfileContext, List.of(), false);
				battlePlayers.computeIfAbsent(id, k -> new ConcurrentHashMap<>()).put(user, playerContext);
			}
			return new JoinBattleResponse.Player(playerContext);
		}

		if (context.playerIds().size() < 2) {
			List<UserId> updatedPlayers = new ArrayList<>(context.playerIds());
			updatedPlayers.add(user);
			battles.put(id, new BattleContext(
					context.battleId(),
					updatedPlayers,
					context.spectatorIds(),
					context.turn(),
					context.phase(),
					context.winnerPlayerId()));
			UserProfileContext userProfileContext = userProfileContextService.getByUserId(user);
			PlayerBattleContext playerContext = new PlayerBattleContext(userProfileContext, List.of(), false);
			battlePlayers.computeIfAbsent(id, k -> new ConcurrentHashMap<>()).put(user, playerContext);
			return new JoinBattleResponse.Player(playerContext);
		}

		if (!context.spectatorIds().contains(user)) {
			List<UserId> updatedSpectators = new ArrayList<>(context.spectatorIds());
			updatedSpectators.add(user);
			battles.put(id, new BattleContext(
					context.battleId(),
					context.playerIds(),
					updatedSpectators,
					context.turn(),
					context.phase(),
					context.winnerPlayerId()));
		}
		UserProfileContext userProfileContext = userProfileContextService.getByUserId(user);
		SpectatorBattleContext spectatorContext = new SpectatorBattleContext(userProfileContext);
		battleSpectators.computeIfAbsent(id, k -> new ConcurrentHashMap<>()).put(user, spectatorContext);
		return new JoinBattleResponse.Spectator(spectatorContext);
	}
}
