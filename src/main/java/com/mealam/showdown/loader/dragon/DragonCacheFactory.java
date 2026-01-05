package com.mealam.showdown.loader.dragon;

import com.mealam.showdown.loader.cache.dragon.DragonCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.dragon.Dragon;

public class DragonCacheFactory implements CacheFactory<DragonCache, Dragon> {

	public static final DragonCacheFactory INSTANCE = new DragonCacheFactory();

	private DragonCacheFactory() {
	}

	@Override
	public DragonCache construct(Dragon pDragon) {
		return DragonCache.construct(pDragon);
	}

}