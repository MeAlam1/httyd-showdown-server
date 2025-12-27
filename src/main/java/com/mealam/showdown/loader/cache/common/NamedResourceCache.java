package com.mealam.showdown.loader.cache.common;

import com.mealam.showdown.loader.json.deserialize.common.NamedResource;

import java.util.List;

public record NamedResourceCache(
		String name,
		String url
) {
	public static NamedResourceCache construct(NamedResource pNamedResource) {
		return new NamedResourceCache(
				pNamedResource.name(),
				pNamedResource.url()
		);
	}

	public static List<NamedResourceCache> construct(List<NamedResource> pNamedResources) {
		return pNamedResources.stream()
				.map(NamedResourceCache::construct)
				.toList();
	}
}