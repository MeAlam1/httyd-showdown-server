/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.cache.dragon.stats.DragonStatsCache;
import com.mealam.showdown.loader.cache.dragon.stats.PhysicalStatsCache;
import com.mealam.showdown.loader.json.deserialize.dragon.Dragon;
import java.util.List;

public record DragonCache(
		String schemaVersion,
		String id,
		String name,
		boolean trainable,
		ClassificationCache classification,
		AppearancesCache appearances,
		BiologyCache biology,
		AbilitiesCache abilities,
		List<String> moveset,
		PhysicalStatsCache physicalStats,
		List<String> individuals,
		List<String> subspecies,
		List<String> hybrids,
		DragonStatsCache stats,
		MediaCache media,
		List<String> tags,
		DragonMetadataCache metadata) {

	public static DragonCache construct(Dragon pSource) {
		return new DragonCache(
				pSource.schemaVersion(),
				pSource.id(),
				pSource.name(),
				pSource.trainable(),
				ClassificationCache.construct(pSource.classification()),
				AppearancesCache.construct(pSource.appearances()),
				BiologyCache.construct(pSource.biology()),
				AbilitiesCache.construct(pSource.abilities()),
				pSource.moveset(),
				PhysicalStatsCache.construct(pSource.physicalStats()),
				pSource.individuals(),
				pSource.subspecies(),
				pSource.hybrids(),
				DragonStatsCache.construct(pSource.stats()),
				MediaCache.construct(pSource.media()),
				pSource.tags(),
				DragonMetadataCache.construct(pSource.metadata()));
	}
}
