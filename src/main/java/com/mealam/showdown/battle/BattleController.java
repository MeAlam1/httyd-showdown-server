/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/battle")
public class BattleController {

	private final BattleService battleService;

	public BattleController(BattleService pBattleService) {
		this.battleService = pBattleService;
	}

	@PostMapping("/create")
	public CreateBattleResponse createBattle(@Valid @RequestBody CreateBattleRequest pRequest) {
		return battleService.createBattle(pRequest.playerId());
	}

	@GetMapping("/{pBattleId}")
	public BattleContext getBattle(@PathVariable String pBattleId) {
		return battleService.getBattle(pBattleId);
	}

	@PostMapping("/{pBattleId}/join")
	public JoinBattleResponse joinBattle(
			@PathVariable String pBattleId,
			@RequestBody JoinBattleRequest pRequest) {
		return battleService.joinBattle(pBattleId, pRequest.userId());
	}
}
