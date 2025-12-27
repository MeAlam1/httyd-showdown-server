package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.Metadata;

public record MetadataCache(
		boolean canonical,
		String introducedIn,
		String lastUpdated
) {
	public static MetadataCache construct(Metadata pData) {
		return new MetadataCache(
				pData.canonical(),
				pData.introducedIn(),
				pData.lastUpdated()
		);
	}
}