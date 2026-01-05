/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.ability;

import com.mealam.showdown.loader.cache.ability.AbilityCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.ability.Ability;

public class AbilityCacheFactory implements CacheFactory<AbilityCache, Ability> {

	public static final AbilityCacheFactory INSTANCE = new AbilityCacheFactory();

	private AbilityCacheFactory() {}

	@Override
	public AbilityCache construct(Ability pAbility) {
		return AbilityCache.construct(pAbility);
	}
}
