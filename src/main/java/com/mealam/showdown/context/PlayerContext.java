/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.context;

import com.mealam.showdown.user.context.UserContext;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record PlayerContext(
		String playerId,
		@Nullable UserContext user,
		List<String> friendIds,
		List<DragonContext> party,
		int totalWins,
		int totalLosses,
		long lastActiveTimestamp,
		String avatarUrl,
		List<String> achievements) {}
