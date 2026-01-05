package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NaturePassiveEffectsEntryParameters;

public record NaturePassiveEffectsEntryParametersCache(
		float multiplier,
		String appliesTo
) {
	public static NaturePassiveEffectsEntryParametersCache construct(NaturePassiveEffectsEntryParameters pNaturePassiveEffectsEntryParameters) {
		return new NaturePassiveEffectsEntryParametersCache(
				pNaturePassiveEffectsEntryParameters.multiplier(),
				pNaturePassiveEffectsEntryParameters.appliesTo()
		);
	}
}
