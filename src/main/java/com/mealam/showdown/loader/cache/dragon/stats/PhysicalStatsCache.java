/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.cache.common.MeasurementCache;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonPhysicalStats;

public record PhysicalStatsCache(
		MeasurementCache length,
		MeasurementCache weight,
		MeasurementCache wingspan) {

	public static PhysicalStatsCache construct(DragonPhysicalStats pSource) {
		return new PhysicalStatsCache(
				MeasurementCache.construct(pSource.length()),
				MeasurementCache.construct(pSource.weight()),
				MeasurementCache.construct(pSource.wingspan()));
	}
}
