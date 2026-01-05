/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.cache.common.MetadataCache;
import com.mealam.showdown.loader.json.deserialize.item.Item;
import java.util.List;

public record ItemCache(
		String schemaVersion,
		String id,
		String name,
		String description,
		String icon,
		List<ItemEffectsEntryCache> effects,
		ItemConditionsCache conditions,
		MetadataCache metadata) {

	public static ItemCache construct(Item pItem) {
		return new ItemCache(
				pItem.schemaVersion(),
				pItem.id(),
				pItem.name(),
				pItem.description(),
				pItem.icon(),
				ItemEffectsEntryCache.construct(pItem.effects()),
				ItemConditionsCache.construct(pItem.conditions()),
				MetadataCache.construct(pItem.metadata()));
	}
}
