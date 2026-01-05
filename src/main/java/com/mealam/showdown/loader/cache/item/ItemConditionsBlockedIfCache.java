package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemConditionsBlockedIf;

public record ItemConditionsBlockedIfCache(
		String dragonSize
) {
	public static ItemConditionsBlockedIfCache construct(ItemConditionsBlockedIf pItemConditionsBlockedIf) {
		return new ItemConditionsBlockedIfCache(
				pItemConditionsBlockedIf.dragonSize()
		);
	}
}
