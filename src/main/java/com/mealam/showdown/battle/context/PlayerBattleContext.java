/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.context;

import com.mealam.showdown.context.PlayerContext;
import java.util.List;

public record PlayerBattleContext(
		PlayerContext player,
		List<DragonBattleContext> party,
		boolean isTurn) {}
