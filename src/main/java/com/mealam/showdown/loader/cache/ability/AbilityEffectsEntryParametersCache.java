package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityEffectsEntryParameters;

public record AbilityEffectsEntryParametersCache(
		String status,
		int durationSeconds,
		int radiusMeters,
		float chance,
		int maxStacks,
		String stackBehavior,
		String resistType
) {
	public static AbilityEffectsEntryParametersCache construct(AbilityEffectsEntryParameters pAbilityEffectsEntryParameters) {
		return new AbilityEffectsEntryParametersCache(
				pAbilityEffectsEntryParameters.status(),
				pAbilityEffectsEntryParameters.durationSeconds(),
				pAbilityEffectsEntryParameters.radiusMeters(),
				pAbilityEffectsEntryParameters.chance(),
				pAbilityEffectsEntryParameters.maxStacks(),
				pAbilityEffectsEntryParameters.stackBehavior(),
				pAbilityEffectsEntryParameters.resistType()
		);
	}
}
