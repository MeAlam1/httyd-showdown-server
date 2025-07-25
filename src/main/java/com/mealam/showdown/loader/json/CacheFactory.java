/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json;

public interface CacheFactory<T, S> {

	T construct(S pSource);

	static <T, S> T constructWithFactory(CacheFactory<T, S> pFactory, S pSource) {
		return pFactory.construct(pSource);
	}
}
