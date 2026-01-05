/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
		TrainingEffortContext trainingEffort) {

	public DragonContext {
		moves = moves == null ? List.of() : List.copyOf(moves);
	}
}
