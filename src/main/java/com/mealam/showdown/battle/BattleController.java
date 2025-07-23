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
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import com.mealam.showdown.battle.dto.response.GetBattleResponse;
import com.mealam.showdown.user.context.UserProfileContext;
import com.mealam.showdown.user.context.UserProfileContextService;
import com.mealam.showdown.user.data.UserId;
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

		UserProfileContext userProfileContext = userProfileContextService.getByUserId(creatorId);

		PlayerBattleContext playerContext = new PlayerBattleContext(
				userProfileContext,
				List.of(), // Dragon Party
				false);
		battlePlayers.put(battleId, new ConcurrentHashMap<>(Map.of(creatorId, playerContext)));

		return new CreateBattleResponse(battleId.value(), context);
	}

	@GetMapping("/{pBattleId}")
	public GetBattleResponse getBattle(
			@PathVariable String pBattleId,
			@RequestParam String pUserId) {
		BattleId battleId = BattleId.parse(pBattleId);
		UserId userId = UserId.parse(pUserId);

		BattleContext context = battles.get(battleId);
		if (context == null) {
			throw new NoSuchElementException("Battle not found");
		}

		PlayerBattleContext playerContext = battlePlayers
				.getOrDefault(battleId, Map.of())
				.get(userId);

		return new GetBattleResponse(context, playerContext);
	}

	@PostMapping("/{pBattleId}/join")
	public PlayerBattleContext joinBattle(@PathVariable String pBattleId, @RequestParam String pUserId) {
		BattleId battleId = BattleId.parse(pBattleId);
		UserId userId = UserId.parse(pUserId);

		BattleContext context = battles.get(battleId);
		if (context == null) throw new NoSuchElementException("Battle not found");

		UserProfileContext userProfileContext = userProfileContextService.getByUserId(userId);

		PlayerBattleContext playerContext = new PlayerBattleContext(
				userProfileContext,
				List.of(), // Dragon Party
				false);
		battlePlayers.computeIfAbsent(battleId, k -> new ConcurrentHashMap<>()).put(userId, playerContext);

		return playerContext;
	}
}
