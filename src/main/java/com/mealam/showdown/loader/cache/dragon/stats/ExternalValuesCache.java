/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonExternalValues;
import org.jetbrains.annotations.Nullable;

public record ExternalValuesCache(
		@Nullable Float attack,
		@Nullable Float speed,
		@Nullable Float armor,
		@Nullable Float firepower,
		@Nullable Float shotLimit,
		@Nullable Float venom,
		@Nullable Float jawStrength,
		@Nullable Float stealth) {

	public static ExternalValuesCache construct(DragonExternalValues pSource) {
		return new ExternalValuesCache(
				pSource.attack(),
				pSource.speed(),
				pSource.armor(),
				pSource.firepower(),
				pSource.shotLimit(),
				pSource.venom(),
				pSource.jawStrength(),
				pSource.stealth());
	}
}
