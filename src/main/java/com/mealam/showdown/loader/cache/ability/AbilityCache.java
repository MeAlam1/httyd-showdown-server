package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.cache.common.MetadataCache;
import com.mealam.showdown.loader.json.deserialize.ability.Ability;

import java.util.List;

public record AbilityCache(
		String schemaVersion,
		String id,
		String name,
		String description,
		String type,
		String target,
		List<AbilityEffectsEntryCache> effects,
		AbilityScalingCache scaling,
		AbilityConditionsCache conditions,
		MetadataCache metadata
) {

	public static AbilityCache construct(Ability pAbility) {
		return new AbilityCache(
				pAbility.schemaVersion(),
				pAbility.id(),
				pAbility.name(),
				pAbility.description(),
				pAbility.type(),
				pAbility.target(),
				AbilityEffectsEntryCache.construct(pAbility.effects()),
				AbilityScalingCache.construct(pAbility.scaling()),
				AbilityConditionsCache.construct(pAbility.conditions()),
				MetadataCache.construct(pAbility.metadata())
		);
	}
}
