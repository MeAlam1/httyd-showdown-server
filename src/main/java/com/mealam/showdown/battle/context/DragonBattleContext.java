/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.context;

import com.mealam.showdown.move.enums.Status;
import java.util.List;

public record DragonBattleContext(
		String dragonId,
		String name,
		int level,
		int currentHp,
		int maxHp,
		Status status,
		List<MoveBattleContext> moves,
		String ownerPlayerId,
		boolean isActive) {}
