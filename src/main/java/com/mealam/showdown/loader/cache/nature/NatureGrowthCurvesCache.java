/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NatureGrowthCurves;

public record NatureGrowthCurvesCache(
		String attack,
		String stamina,
		String critChance) {

	public static NatureGrowthCurvesCache construct(NatureGrowthCurves pNatureGrowthCurves) {
		return new NatureGrowthCurvesCache(
				pNatureGrowthCurves.attack(),
				pNatureGrowthCurves.stamina(),
				pNatureGrowthCurves.critChance());
	}
}
