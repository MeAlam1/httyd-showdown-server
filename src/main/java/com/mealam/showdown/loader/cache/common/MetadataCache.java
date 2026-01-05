package com.mealam.showdown.loader.cache.common;

import com.mealam.showdown.loader.json.deserialize.common.Metadata;

public record MetadataCache(String introducedIn,
                            String lastUpdated) {
	public static MetadataCache construct(Metadata pMetadata) {
		return new MetadataCache(
				pMetadata.introducedIn(),
				pMetadata.lastUpdated()
		);
	}
}

