/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.cache.common.MetadataCache;
import com.mealam.showdown.loader.json.deserialize.dragon.DragonMetadata;

public record DragonMetadataCache(
		boolean canonical,
		MetadataCache metadata) {

	public static DragonMetadataCache construct(DragonMetadata pData) {
		return new DragonMetadataCache(
				pData.canonical(),
				MetadataCache.construct(pData.metadata()));
	}
}
