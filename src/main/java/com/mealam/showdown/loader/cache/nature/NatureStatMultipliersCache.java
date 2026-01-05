package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NatureStatMultipliers;

public record NatureStatMultipliersCache(
		float attack,
		float stamina,
		float defense,
		float critChance,
		float speed
) {
	public static NatureStatMultipliersCache construct(NatureStatMultipliers pNatureStatMultipliers) {
		return new NatureStatMultipliersCache(
				pNatureStatMultipliers.attack(),
				pNatureStatMultipliers.stamina(),
				pNatureStatMultipliers.defense(),
				pNatureStatMultipliers.critChance(),
				pNatureStatMultipliers.speed()
		);
	}
}
