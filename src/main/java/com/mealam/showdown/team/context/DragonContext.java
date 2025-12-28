package com.mealam.showdown.team.context;

import com.mealam.showdown.team.context.dragon.*;

import java.util.List;

public record DragonContext(
		String dragonId,
		String nickname,
		NatureContext nature,
		LevelContext level,
		AbilityContext ability,
		StatsContext stats,
		HeldItemContext heldItem,
		List<MoveContext> moves,
		TrainingEffortContext trainingEffort
) {
	public DragonContext {
		moves = moves == null ? List.of() : List.copyOf(moves);
	}
}