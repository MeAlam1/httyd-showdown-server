/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.DragonAppearances;
import java.util.List;

public record AppearancesCache(
		List<String> movies,
		List<String> series,
		List<String> games) {

	public static AppearancesCache construct(DragonAppearances pAppearance) {
		return new AppearancesCache(
				pAppearance.movies(),
				pAppearance.series(),
				pAppearance.games());
	}
}
