/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemConditionsBlockedIf;

public record ItemConditionsBlockedIfCache(
		String dragonSize) {

	public static ItemConditionsBlockedIfCache construct(ItemConditionsBlockedIf pItemConditionsBlockedIf) {
		return new ItemConditionsBlockedIfCache(
				pItemConditionsBlockedIf.dragonSize());
	}
}
