/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.context;

import com.mealam.showdown.loader.cache.moves.MovesCache;
import org.jetbrains.annotations.Nullable;

public record MoveContext(
		MovesCache baseData,
		String userDragonId,
		@Nullable String targetDragonId) {}
