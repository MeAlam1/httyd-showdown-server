/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.cache.common.NamedResourceCache;
import com.mealam.showdown.loader.json.deserialize.dragon.DragonMedia;
import java.util.List;

public record MediaCache(
		List<NamedResourceCache> images,
		List<NamedResourceCache> videos,
		List<NamedResourceCache> sounds) {

	public static MediaCache construct(DragonMedia pMedia) {
		return new MediaCache(
				NamedResourceCache.construct(pMedia.images()),
				NamedResourceCache.construct(pMedia.videos()),
				NamedResourceCache.construct(pMedia.sounds()));
	}
}
