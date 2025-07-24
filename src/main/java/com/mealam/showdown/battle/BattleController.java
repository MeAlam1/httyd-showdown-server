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
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.turns.Turns;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import com.mealam.showdown.user.context.UserProfileContext;
import com.mealam.showdown.user.context.UserProfileContextService;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/battle")
public class BattleController {

	private final Map<BattleId, BattleContext> battles = new ConcurrentHashMap<>();
	private final Map<BattleId, Map<UserId, PlayerBattleContext>> battlePlayers = new ConcurrentHashMap<>();
	private final UserProfileContextService userProfileContextService;

	public BattleController(UserProfileContextService pUserProfileContextService) {
		this.userProfileContextService = pUserProfileContextService;
	}

	@PostMapping("/create")
	public CreateBattleResponse createBattle(@RequestBody CreateBattleRequest pRequest) {
		BattleId battleId = BattleId.generate();
		UserId creatorId = UserId.parse(pRequest.playerId());
		BattleContext context = new BattleContext(
				battleId,
				List.of(creatorId),
				new Turns().createBattle(),
				Phase.REGISTRY.defaultVersion(),
				null);
		battles.put(battleId, context);

		Logger.log(LogLevel.INFO, "Battle created, ID: " + battleId.value() + ", Creator: " + creatorId.value());

		UserProfileContext userProfileContext = userProfileContextService.getByUserId(creatorId);

		PlayerBattleContext playerContext = new PlayerBattleContext(
				userProfileContext,
				List.of(), // Dragon Party
				false);
		battlePlayers.put(battleId, new ConcurrentHashMap<>(Map.of(creatorId, playerContext)));

		return new CreateBattleResponse(battleId.value(), context);
	}

	@GetMapping("/{pBattleId}")
	public BattleContext getBattle(
			@PathVariable String pBattleId) {
		BattleId battleId = BattleId.parse(pBattleId);

		Logger.log(LogLevel.INFO, "Battle created, ID: " + battleId.value());

		BattleContext context = battles.get(battleId);
		if (context == null) {
			throw new NoSuchElementException("Battle not found, all Battles: " + battles);
		}

		return context;
	}

	@PostMapping("/{pBattleId}/join")
	public PlayerBattleContext joinBattle(
			@PathVariable String pBattleId,
			@RequestBody JoinBattleRequest request) {
		BattleId battleId = BattleId.parse(pBattleId);
		UserId userId = UserId.parse(request.userId());

		BattleContext context = battles.get(battleId);
		if (context == null) {
			throw new NoSuchElementException("Battle not found, all Battles: " + battles);
		}

		Logger.log(LogLevel.INFO, "Battle created, ID: " + battleId.value() + ", Creator: " + userId.value());

		UserProfileContext userProfileContext = userProfileContextService.getByUserId(userId);

		PlayerBattleContext playerContext = new PlayerBattleContext(
				userProfileContext,
				List.of(), // Dragon Party
				false);
		battlePlayers.computeIfAbsent(battleId, k -> new ConcurrentHashMap<>()).put(userId, playerContext);

		return playerContext;
	}
}
