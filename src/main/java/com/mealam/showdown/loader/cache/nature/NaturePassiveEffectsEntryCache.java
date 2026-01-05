package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NaturePassiveEffectsEntry;

import java.util.List;

public record NaturePassiveEffectsEntryCache(
		String effectId,
		NaturePassiveEffectsEntryParametersCache parameters
) {
	public static NaturePassiveEffectsEntryCache construct(NaturePassiveEffectsEntry pNaturePassiveEffectsEntry) {
		return new NaturePassiveEffectsEntryCache(
				pNaturePassiveEffectsEntry.effectId(),
				NaturePassiveEffectsEntryParametersCache.construct(pNaturePassiveEffectsEntry.parameters())
		);
	}

	public static List<NaturePassiveEffectsEntryCache> construct(List<NaturePassiveEffectsEntry> pNaturePassiveEffectsEntryList) {
		return pNaturePassiveEffectsEntryList.stream()
				.map(NaturePassiveEffectsEntryCache::construct)
				.toList();
	}
}
