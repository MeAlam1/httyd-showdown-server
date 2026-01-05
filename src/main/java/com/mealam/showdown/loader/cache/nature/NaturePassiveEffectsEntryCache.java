/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NaturePassiveEffectsEntry;
import java.util.List;

public record NaturePassiveEffectsEntryCache(
		String effectId,
		NaturePassiveEffectsEntryParametersCache parameters) {

	public static NaturePassiveEffectsEntryCache construct(NaturePassiveEffectsEntry pNaturePassiveEffectsEntry) {
		return new NaturePassiveEffectsEntryCache(
				pNaturePassiveEffectsEntry.effectId(),
				NaturePassiveEffectsEntryParametersCache.construct(pNaturePassiveEffectsEntry.parameters()));
	}

	public static List<NaturePassiveEffectsEntryCache> construct(List<NaturePassiveEffectsEntry> pNaturePassiveEffectsEntryList) {
		return pNaturePassiveEffectsEntryList.stream()
				.map(NaturePassiveEffectsEntryCache::construct)
				.toList();
	}
}
