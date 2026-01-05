/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityConditionsRequiresItemsEntry;
import java.util.List;

public record AbilityConditionsRequiresItemsEntryCache(
		String itemId,
		boolean consume) {

	public static AbilityConditionsRequiresItemsEntryCache construct(AbilityConditionsRequiresItemsEntry pAbilityConditionsRequiresItemsEntry) {
		return new AbilityConditionsRequiresItemsEntryCache(
				pAbilityConditionsRequiresItemsEntry.itemId(),
				pAbilityConditionsRequiresItemsEntry.consume());
	}

	public static List<AbilityConditionsRequiresItemsEntryCache> construct(List<AbilityConditionsRequiresItemsEntry> pAbilityConditionsRequiresItemsEntry) {
		return pAbilityConditionsRequiresItemsEntry.stream()
				.map(AbilityConditionsRequiresItemsEntryCache::construct)
				.toList();
	}
}
