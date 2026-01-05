package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityConditions;
import com.mealam.showdown.loader.json.deserialize.ability.AbilityConditionsRequiresItemsEntry;
import com.mealam.showdown.loader.json.deserialize.ability.AbilityEffectsEntryParameters;

import java.util.List;

public record AbilityConditionsCache(
		List<String> blockedIn,
		List<String> requiresTagOnCaster,
		List<AbilityConditionsRequiresItemsEntryCache> requiresItems
) {

	public static AbilityConditionsCache construct(AbilityConditions pAbilityConditions) {
		return new AbilityConditionsCache(
				pAbilityConditions.blockedIn(),
				pAbilityConditions.requiresTagOnCaster(),
				AbilityConditionsRequiresItemsEntryCache.construct(pAbilityConditions.requiresItems())
		);
	}
}
