/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.SpectatorBattleContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/battle/spectator")
public class SpectatorController {

	@PostMapping("/join")
	public SpectatorBattleContext joinAsSpectator(@RequestParam String pBattleId, @RequestParam String pSpectatorId, @RequestParam String pUsername) {
		// Stub: Replace with actual service call
		return new SpectatorBattleContext(pSpectatorId, pUsername);
	}
}
