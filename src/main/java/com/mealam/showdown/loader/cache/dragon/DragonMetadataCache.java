package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.cache.common.MetadataCache;
import com.mealam.showdown.loader.json.deserialize.dragon.DragonMetadata;

public record DragonMetadataCache(
		boolean canonical,
		MetadataCache metadata
) {
	public static DragonMetadataCache construct(DragonMetadata pData) {
		return new DragonMetadataCache(
				pData.canonical(),
				MetadataCache.construct(pData.metadata())
		);
	}
}