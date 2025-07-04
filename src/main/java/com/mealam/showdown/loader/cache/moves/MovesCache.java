/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.moves;

import com.mealam.showdown.moves.enums.Category;
import com.mealam.showdown.moves.enums.Type;

public record MovesCache(
		String id,
		String name,
		String description,
		Type type,
		Category category,
		Integer power,
		Integer accuracy,
		Integer pp,
		EffectCache effect) {
}
