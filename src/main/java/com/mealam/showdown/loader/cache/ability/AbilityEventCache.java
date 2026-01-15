/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.ability;

import com.mealam.showdown.loader.json.deserialize.ability.AbilityEvent;

import javax.swing.*;
import java.util.List;

public record AbilityEventCache() {

	public static AbilityEventCache construct(AbilityEvent pAbility) {
		return new AbilityEventCache(
		);
	}

	public static List<AbilityEventCache> construct(List<AbilityEvent> pAbility) {
		return pAbility.stream()
				.map(AbilityEventCache::construct)
				.toList();
	}
}
