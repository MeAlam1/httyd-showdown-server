package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.cache.common.MeasurementCache;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.PhysicalStats;

public record PhysicalStatsCache(
		MeasurementCache length,
		MeasurementCache weight,
		MeasurementCache wingspan
) {
	public static PhysicalStatsCache construct(PhysicalStats pSource) {
		return new PhysicalStatsCache(
				MeasurementCache.construct(pSource.length()),
				MeasurementCache.construct(pSource.weight()),
				MeasurementCache.construct(pSource.wingspan())
		);
	}
}