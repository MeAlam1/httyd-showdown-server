package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityScaling;

public record AbilityScalingCache(
		String withStat,
		float multiplier,
		float perLevelMultiplier,
		int maxMultiplier,
		int capAtLevel
) {
	public static AbilityScalingCache construct(AbilityScaling pAbilityScaling) {
		return new AbilityScalingCache(
				pAbilityScaling.withStat(),
				pAbilityScaling.multiplier(),
				pAbilityScaling.perLevelMultiplier(),
				pAbilityScaling.maxMultiplier(),
				pAbilityScaling.capAtLevel()
		);
	}
}
