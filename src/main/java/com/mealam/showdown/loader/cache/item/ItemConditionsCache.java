package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemConditions;
import com.mealam.showdown.loader.json.deserialize.item.ItemConditionsBlockedIf;

import java.util.List;

public record ItemConditionsCache(
		List<String> requiresTags,
		ItemConditionsBlockedIfCache blockedIf,
		int minLevel
) {
	
	public static ItemConditionsCache construct(ItemConditions pItemConditions) {
		return new ItemConditionsCache(
				pItemConditions.requiresTags(),
				ItemConditionsBlockedIfCache.construct(pItemConditions.blockedIf()),
				pItemConditions.minLevel()
		);
	}
}
