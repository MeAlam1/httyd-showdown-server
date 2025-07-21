package com.mealam.showdown.battle.context;

import java.util.List;
import java.util.Map;

import com.mealam.showdown.battle.data.Phase;
import org.jetbrains.annotations.Nullable;

public record BattleContext(
		String battleId,
		List<String> playerIds,
		Map<String, DragonBattleContext> dragons, // dragonId -> DragonBattleContext
		int turnNumber,
		Map<String, String> activeMoves, // dragonId -> moveId
		Phase phase,
		@Nullable String winnerPlayerId) {}
