package com.mealam.showdown.loader.cache.item;

import com.mealam.showdown.loader.json.deserialize.item.ItemEffectsEntryParameters;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ItemEffectsEntryParametersCache(
		String element,
		float multiplier,
		@Nullable String durationSeconds,
		List<String> appliesTo
) {
	public static ItemEffectsEntryParametersCache construct(ItemEffectsEntryParameters pItemEffectsEntryParameters) {
		return new ItemEffectsEntryParametersCache(
				pItemEffectsEntryParameters.element(),
				pItemEffectsEntryParameters.multiplier(),
				pItemEffectsEntryParameters.durationSeconds(),
				pItemEffectsEntryParameters.appliesTo()
		);
	}
}
