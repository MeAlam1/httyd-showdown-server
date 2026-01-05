package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemEffectsEntry;
import com.mealam.showdown.loader.json.deserialize.item.ItemEffectsEntryParameters;

import java.util.List;

public record ItemEffectsEntryCache(
		String effectId,
		ItemEffectsEntryParametersCache parameters
) {

	public static ItemEffectsEntryCache construct(ItemEffectsEntry pItemEffectsEntry) {
		return new ItemEffectsEntryCache(
				pItemEffectsEntry.effectId(),
				ItemEffectsEntryParametersCache.construct(pItemEffectsEntry.parameters())
		);
	}

	public static List<ItemEffectsEntryCache> construct(List<ItemEffectsEntry> pItemEffectsEntry) {
		return pItemEffectsEntry.stream().map(ItemEffectsEntryCache::construct).toList();
	}
}
