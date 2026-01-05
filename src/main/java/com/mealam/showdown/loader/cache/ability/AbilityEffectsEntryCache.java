package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityEffectsEntry;
import com.mealam.showdown.loader.json.deserialize.ability.AbilityEffectsEntryParameters;

import java.util.List;

public record AbilityEffectsEntryCache(
		String effectId,
		AbilityEffectsEntryParametersCache parameters
) {
	public static AbilityEffectsEntryCache construct(AbilityEffectsEntry pAbilityEffectsEntry) {
		return new AbilityEffectsEntryCache(
				pAbilityEffectsEntry.effectId(),
				AbilityEffectsEntryParametersCache.construct(pAbilityEffectsEntry.parameters())
		);
	}

	public static List<AbilityEffectsEntryCache> construct(List<AbilityEffectsEntry> pAbilityEffectsEntry) {
		return pAbilityEffectsEntry.stream()
				.map(AbilityEffectsEntryCache::construct)
				.toList();
	}
}
