/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.DragonBiology;
import java.util.List;

public record BiologyCache(
		List<String> diet,
		String temperament,
		List<String> fireType,
		List<String> features,
		List<String> colors) {

	public static BiologyCache construct(DragonBiology pBiology) {
		return new BiologyCache(
				pBiology.diet(),
				pBiology.temperament(),
				pBiology.fireType(),
				pBiology.features(),
				pBiology.colors());
	}
}
