/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.move.data.MoveId;
import com.mealam.showdown.user.data.UserId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/battle/player")
public class PlayerController {

	@PostMapping("/join")
	public PlayerBattleContext joinBattle(@RequestParam String pBattleId, @RequestParam String pUserId) {
		BattleId battleId = BattleId.parse(pBattleId);
		UserId userId = UserId.parse(pUserId);
		// Stub: Replace with actual service call
		return null;
	}

	@PostMapping("/move")
	public String makeMove(@RequestParam String pBattleId, @RequestParam String pUserId, @RequestParam String pMoveId) {
		BattleId battleId = BattleId.parse(pBattleId);
		UserId userId = UserId.parse(pUserId);
		MoveId moveId = MoveId.parse(pMoveId);
		// Stub: Replace with actual service call
		return "Move registered";
	}
}
