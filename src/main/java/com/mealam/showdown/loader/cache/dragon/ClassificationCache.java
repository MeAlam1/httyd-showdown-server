/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragon;

import com.google.gson.annotations.SerializedName;
import com.mealam.showdown.loader.json.deserialize.dragon.DragonClassification;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record ClassificationCache(
		@SerializedName("class") List<String> clazz,
		@Nullable String former,
		String size,
		String habitat) {

	public static ClassificationCache construct(DragonClassification pClassification) {
		return new ClassificationCache(
				pClassification.clazz(),
				pClassification.former(),
				pClassification.size(),
				pClassification.habitat());
	}
}
