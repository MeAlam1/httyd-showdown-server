package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonStats;

public record DragonStatsCache(
		InternalStatsCache internal,
		ExternalStatsCache external
) {
	public static DragonStatsCache construct(DragonStats pSource) {
		return new DragonStatsCache(
				InternalStatsCache.construct(pSource.internal()),
				ExternalStatsCache.construct(pSource.external())
		);
	}
}