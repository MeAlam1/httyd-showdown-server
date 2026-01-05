package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NatureGrowthCurves;

public record NatureGrowthCurvesCache(
		String attack,
		String stamina,
		String critChance
) {
	public static NatureGrowthCurvesCache construct(NatureGrowthCurves pNatureGrowthCurves) {
		return new NatureGrowthCurvesCache(
				pNatureGrowthCurves.attack(),
				pNatureGrowthCurves.stamina(),
				pNatureGrowthCurves.critChance()
		);
	}
}
