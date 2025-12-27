package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.json.deserialize.dragon.stats.ExternalStats;

import java.util.List;

public record ExternalStatsCache(
		List<String> notes,
		ExternalValuesCache values
) {
	public static ExternalStatsCache construct(ExternalStats pSource) {
		return new ExternalStatsCache(
				pSource.notes(),
				ExternalValuesCache.construct(pSource.values())
		);
	}
}