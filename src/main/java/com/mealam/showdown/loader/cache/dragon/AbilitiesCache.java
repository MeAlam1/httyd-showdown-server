/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.DragonAbilities;

public record AbilitiesCache() {

	public static AbilitiesCache construct(DragonAbilities pSource) {
		return new AbilitiesCache();
	}
}
