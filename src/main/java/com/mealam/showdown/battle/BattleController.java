/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.turns.Turns;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/battle")
public class BattleController {

	private final Map<BattleId, BattleContext> battles = new ConcurrentHashMap<>();

	@PostMapping("/create")
	public CreateBattleResponse createBattle(@RequestBody CreateBattleRequest pRequest) {
		BattleId battleId = BattleId.generate();
		BattleContext context = new BattleContext(
				battleId.value(),
				List.of(pRequest.playerId()),
				Map.of(),
				Map.of(),
				new Turns().createBattle(),
				Phase.REGISTRY.defaultVersion(),
				null);
		battles.put(battleId, context);
		return new CreateBattleResponse(battleId.value(), context);
	}

	@GetMapping("/{pBattleId}")
	public BattleContext getBattle(@PathVariable String pBattleId) {
		BattleId battleId = BattleId.parse(pBattleId);
		BattleContext context = battles.get(battleId);
		if (context == null) {
			throw new NoSuchElementException("Battle not found");
		}
		return context;
	}
}
