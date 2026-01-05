/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityEffectsEntryParameters;

public record AbilityEffectsEntryParametersCache(
		String status,
		int durationSeconds,
		int radiusMeters,
		float chance,
		int maxStacks,
		String stackBehavior,
		String resistType) {

	public static AbilityEffectsEntryParametersCache construct(AbilityEffectsEntryParameters pAbilityEffectsEntryParameters) {
		return new AbilityEffectsEntryParametersCache(
				pAbilityEffectsEntryParameters.status(),
				pAbilityEffectsEntryParameters.durationSeconds(),
				pAbilityEffectsEntryParameters.radiusMeters(),
				pAbilityEffectsEntryParameters.chance(),
				pAbilityEffectsEntryParameters.maxStacks(),
				pAbilityEffectsEntryParameters.stackBehavior(),
				pAbilityEffectsEntryParameters.resistType());
	}
}
