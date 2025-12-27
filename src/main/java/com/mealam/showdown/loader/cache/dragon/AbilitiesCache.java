package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.Abilities;

public record AbilitiesCache() {
	public static AbilitiesCache construct(Abilities pSource) {
		return new AbilitiesCache();
	}
}