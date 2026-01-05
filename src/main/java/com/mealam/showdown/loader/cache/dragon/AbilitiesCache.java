package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.DragonAbilities;

public record AbilitiesCache() {
	public static AbilitiesCache construct(DragonAbilities pSource) {
		return new AbilitiesCache();
	}
}