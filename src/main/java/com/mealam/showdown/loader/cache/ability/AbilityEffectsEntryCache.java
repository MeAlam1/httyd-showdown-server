/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityEffectsEntry;
import java.util.List;

public record AbilityEffectsEntryCache(
		String effectId,
		AbilityEffectsEntryParametersCache parameters) {

	public static AbilityEffectsEntryCache construct(AbilityEffectsEntry pAbilityEffectsEntry) {
		return new AbilityEffectsEntryCache(
				pAbilityEffectsEntry.effectId(),
				AbilityEffectsEntryParametersCache.construct(pAbilityEffectsEntry.parameters()));
	}

	public static List<AbilityEffectsEntryCache> construct(List<AbilityEffectsEntry> pAbilityEffectsEntry) {
		return pAbilityEffectsEntry.stream()
				.map(AbilityEffectsEntryCache::construct)
				.toList();
	}
}
