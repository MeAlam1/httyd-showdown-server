/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonStats;

public record DragonStatsCache(
		InternalStatsCache internal,
		ExternalStatsCache external) {

	public static DragonStatsCache construct(DragonStats pSource) {
		return new DragonStatsCache(
				InternalStatsCache.construct(pSource.internal()),
				ExternalStatsCache.construct(pSource.external()));
	}
}
