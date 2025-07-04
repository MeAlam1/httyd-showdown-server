package com.mealam.showdown.loader.json;

public interface CacheFactory<T, S> {

	T construct(S pSource);

	static <T, S> T constructWithFactory(CacheFactory<T, S> pFactory, S pSource) {
		return pFactory.construct(pSource);
	}
}
