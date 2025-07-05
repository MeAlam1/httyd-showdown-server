package com.mealam.showdown.context.battle;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record BattleContext(
		String battleId,
		List<String> playerIds,
		Map<String, DragonBattleContext> dragons, // dragonId -> DragonBattleContext
		int turnNumber,
		Map<String, String> activeMoves, // dragonId -> moveId
		String phase, // e.g., "START", "IN_PROGRESS", "FINISHED"
		@Nullable String winnerPlayerId
) {
}