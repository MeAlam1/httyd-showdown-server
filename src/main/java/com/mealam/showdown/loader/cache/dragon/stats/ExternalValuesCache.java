package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.json.deserialize.dragon.stats.ExternalValues;
import org.jetbrains.annotations.Nullable;

public record ExternalValuesCache(
		@Nullable Float attack,
		@Nullable Float speed,
		@Nullable Float armor,
		@Nullable Float firepower,
		@Nullable Float shotLimit,
		@Nullable Float venom,
		@Nullable Float jawStrength,
		@Nullable Float stealth
) {
	public static ExternalValuesCache construct(ExternalValues pSource) {
		return new ExternalValuesCache(
				pSource.attack(),
				pSource.speed(),
				pSource.armor(),
				pSource.firepower(),
				pSource.shotLimit(),
				pSource.venom(),
				pSource.jawStrength(),
				pSource.stealth()
		);
	}
}