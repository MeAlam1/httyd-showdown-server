/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
		AbilityRequirementsCache requirements,
		List<AbilityEventCache> events,
		MetadataCache metadata) {

	public static AbilityCache construct(Ability pAbility) {
		return new AbilityCache(
				pAbility.schemaVersion(),
				pAbility.id(),
				pAbility.name(),
				pAbility.description(),
				pAbility.type(),
				AbilityRequirementsCache.construct(pAbility.requirements()),
				AbilityEventCache.construct(pAbility.events()),
				MetadataCache.construct(pAbility.metadata()));
	}
}
