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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/battle")
public class BattleController {

	private final Map<String, BattleContext> battles = new ConcurrentHashMap<>();

	@PostMapping("/create")
	public CreateBattleResponse createBattle(@RequestBody CreateBattleRequest pRequest) {
		String battleId = BattleId.generate().value();
		BattleContext context = new BattleContext(
				battleId,
				List.of(pRequest.playerId()),
				Map.of(),
				new Turns().createBattle(),
				Map.of(),
				Phase.REGISTRY.defaultVersion(),
				null);
		battles.put(battleId, context);
		return new CreateBattleResponse(battleId, context);
	}

	@GetMapping("/{pBattleId}")
	public BattleContext getBattle(@PathVariable String pBattleId) {
		BattleContext context = battles.get(pBattleId);
		if (context == null) {
			throw new NoSuchElementException("Battle not found");
		}
		return context;
	}
}
