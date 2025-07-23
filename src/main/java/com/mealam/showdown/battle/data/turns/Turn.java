/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data.turns;

import com.mealam.showdown.user.data.UserId;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public record Turn(
		int number,
		@Nullable Map<UserId, String> actions // PlayerID -> action (e.g., "move Thunderbolt")
) {}
