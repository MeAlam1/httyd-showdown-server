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
import com.mealam.showdown.user.data.UserId;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record BattleContext(
		String battleId,
		List<String> playerIds,
		Map<String, DragonBattleContext> dragons, // dragonId -> DragonBattleContext
		Map<String, String> activeMoves, // dragonId -> moveId
		Turn turn,
		Phase phase,
		@Nullable UserId winnerPlayerId) {
}
