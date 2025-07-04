package com.mealam.showdown.battle;

import com.mealam.showdown.dragons.DragonContext;
import com.mealam.showdown.players.PlayerContext;

import java.util.List;
import java.util.Map;

public record BattleContext(
		String battleId,
		List<PlayerContext> players,
		Map<String, DragonContext> dragons, // dragonId -> DragonContext
		int turnNumber,
		String status,
		Map<String, String> activeMoves // dragonId -> moveId
) {
}