package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.cache.common.MetadataCache;
import com.mealam.showdown.loader.json.deserialize.nature.Nature;

import java.util.List;

public record NatureCache(
		String schemaVersion,
		String id,
		String name,
		String description,
		String type,
		List<String> tags,
		NatureStatMultipliersCache statMultipliers,
		NatureGrowthCurvesCache growthCurves,
		List<NaturePassiveEffectsEntryCache> passiveEffects,
		MetadataCache metadata
) {

	public static NatureCache construct(Nature pNature) {
		return new NatureCache(
				pNature.schemaVersion(),
				pNature.id(),
				pNature.name(),
				pNature.description(),
				pNature.type(),
				pNature.tags(),
				NatureStatMultipliersCache.construct(pNature.statMultipliers()),
				NatureGrowthCurvesCache.construct(pNature.growthCurves()),
				NaturePassiveEffectsEntryCache.construct(pNature.passiveEffects()),
				MetadataCache.construct(pNature.metadata())
		);
	}
}
