/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.common;

import com.mealam.showdown.loader.json.deserialize.common.Metadata;

public record MetadataCache(String introducedIn,
		String lastUpdated) {

	public static MetadataCache construct(Metadata pMetadata) {
		return new MetadataCache(
				pMetadata.introducedIn(),
				pMetadata.lastUpdated());
	}
}
