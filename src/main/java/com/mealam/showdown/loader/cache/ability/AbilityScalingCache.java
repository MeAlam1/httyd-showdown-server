/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityScaling;

public record AbilityScalingCache(
		String withStat,
		float multiplier,
		float perLevelMultiplier,
		int maxMultiplier,
		int capAtLevel) {

	public static AbilityScalingCache construct(AbilityScaling pAbilityScaling) {
		return new AbilityScalingCache(
				pAbilityScaling.withStat(),
				pAbilityScaling.multiplier(),
				pAbilityScaling.perLevelMultiplier(),
				pAbilityScaling.maxMultiplier(),
				pAbilityScaling.capAtLevel());
	}
}
