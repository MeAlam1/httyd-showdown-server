/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemEffectsEntry;
import java.util.List;

public record ItemEffectsEntryCache(
		String effectId,
		ItemEffectsEntryParametersCache parameters) {

	public static ItemEffectsEntryCache construct(ItemEffectsEntry pItemEffectsEntry) {
		return new ItemEffectsEntryCache(
				pItemEffectsEntry.effectId(),
				ItemEffectsEntryParametersCache.construct(pItemEffectsEntry.parameters()));
	}

	public static List<ItemEffectsEntryCache> construct(List<ItemEffectsEntry> pItemEffectsEntry) {
		return pItemEffectsEntry.stream().map(ItemEffectsEntryCache::construct).toList();
	}
}
