/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.nature;

import com.mealam.showdown.loader.cache.nature.NatureCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.nature.Nature;

public class NatureCacheFactory implements CacheFactory<NatureCache, Nature> {

	public static final NatureCacheFactory INSTANCE = new NatureCacheFactory();

	private NatureCacheFactory() {
	}

	@Override
	public NatureCache construct(Nature pNature) {
		return NatureCache.construct(pNature);
	}

}
