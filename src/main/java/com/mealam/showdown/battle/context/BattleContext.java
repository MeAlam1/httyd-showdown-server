/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.turns.Turn;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public record BattleContext(
		String battleId,
		List<String> playerIds,
		Map<String, DragonBattleContext> dragons, // dragonId -> DragonBattleContext
		Turn turn,
		Map<String, String> activeMoves, // dragonId -> moveId
		Phase phase,
		@Nullable String winnerPlayerId) {}
