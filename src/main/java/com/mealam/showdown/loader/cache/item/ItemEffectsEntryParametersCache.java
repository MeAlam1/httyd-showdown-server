/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemEffectsEntryParameters;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record ItemEffectsEntryParametersCache(
		String element,
		float multiplier,
		@Nullable String durationSeconds,
		List<String> appliesTo) {

	public static ItemEffectsEntryParametersCache construct(ItemEffectsEntryParameters pItemEffectsEntryParameters) {
		return new ItemEffectsEntryParametersCache(
				pItemEffectsEntryParameters.element(),
				pItemEffectsEntryParameters.multiplier(),
				pItemEffectsEntryParameters.durationSeconds(),
				pItemEffectsEntryParameters.appliesTo());
	}
}
