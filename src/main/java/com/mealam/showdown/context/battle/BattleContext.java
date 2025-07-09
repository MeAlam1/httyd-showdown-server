package com.mealam.showdown.context.battle;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public record BattleContext(
		String battleId,
		List<String> playerIds,
		Map<String, DragonBattleContext> dragons, // dragonId -> DragonBattleContext
		int turnNumber,
		Map<String, String> activeMoves, // dragonId -> moveId
		String phase, // e.g., "START", "IN_PROGRESS", "FINISHED"
		@Nullable String winnerPlayerId) {}
