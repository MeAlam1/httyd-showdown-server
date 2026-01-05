/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemConditions;
import java.util.List;

public record ItemConditionsCache(
		List<String> requiresTags,
		ItemConditionsBlockedIfCache blockedIf,
		int minLevel) {

	public static ItemConditionsCache construct(ItemConditions pItemConditions) {
		return new ItemConditionsCache(
				pItemConditions.requiresTags(),
				ItemConditionsBlockedIfCache.construct(pItemConditions.blockedIf()),
				pItemConditions.minLevel());
	}
}
