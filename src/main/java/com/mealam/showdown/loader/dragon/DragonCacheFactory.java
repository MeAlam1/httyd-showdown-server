/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.dragon;

import com.mealam.showdown.loader.cache.dragon.DragonCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.dragon.Dragon;

public class DragonCacheFactory implements CacheFactory<DragonCache, Dragon> {

	public static final DragonCacheFactory INSTANCE = new DragonCacheFactory();

	private DragonCacheFactory() {}

	@Override
	public DragonCache construct(Dragon pDragon) {
		return DragonCache.construct(pDragon);
	}
}
