package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.PlayerBattleContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/battle/player")
public class PlayerController {

	@PostMapping("/join")
	public PlayerBattleContext joinBattle(@RequestParam String pBattleId, @RequestParam String pPlayerId) {
		// Stub: Replace with actual service call
		return null;
	}

	@PostMapping("/move")
	public String makeMove(@RequestParam String pBattleId, @RequestParam String pPlayerId, @RequestParam String pMoveId) {
		// Stub: Replace with actual service call
		return "Move registered";
	}
}