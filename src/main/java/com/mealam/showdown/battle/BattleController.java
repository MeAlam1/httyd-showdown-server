package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.request.CreateBattleRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/battle")
public class BattleController {

	@PostMapping("/create")
	public BattleContext createBattle(@RequestBody CreateBattleRequest request) {
		return new BattleContext(
				"battle-1",
				request.playerIds(),
				Map.of(),
				1,
				Map.of(),
				Phase.REGISTRY.defaultVersion(),
				null
		);
	}

	@GetMapping("/{pBattleId}")
	public BattleContext getBattle(@PathVariable String pBattleId) {
		// Stub: Replace with actual service call
		return null;
	}
}