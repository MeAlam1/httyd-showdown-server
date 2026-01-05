/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityConditions;
import java.util.List;

public record AbilityConditionsCache(
		List<String> blockedIn,
		List<String> requiresTagOnCaster,
		List<AbilityConditionsRequiresItemsEntryCache> requiresItems) {

	public static AbilityConditionsCache construct(AbilityConditions pAbilityConditions) {
		return new AbilityConditionsCache(
				pAbilityConditions.blockedIn(),
				pAbilityConditions.requiresTagOnCaster(),
				AbilityConditionsRequiresItemsEntryCache.construct(pAbilityConditions.requiresItems()));
	}
}
