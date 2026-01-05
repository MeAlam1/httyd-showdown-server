/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.nature;

import com.mealam.showdown.loader.json.deserialize.nature.NatureStatMultipliers;

public record NatureStatMultipliersCache(
		float attack,
		float stamina,
		float defense,
		float critChance,
		float speed) {

	public static NatureStatMultipliersCache construct(NatureStatMultipliers pNatureStatMultipliers) {
		return new NatureStatMultipliersCache(
				pNatureStatMultipliers.attack(),
				pNatureStatMultipliers.stamina(),
				pNatureStatMultipliers.defense(),
				pNatureStatMultipliers.critChance(),
				pNatureStatMultipliers.speed());
	}
}
