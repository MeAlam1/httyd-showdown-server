/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.item.ItemCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.JsonLoader;
import com.mealam.showdown.loader.json.deserialize.item.*;
import com.mealam.showdown.utils.json.SharedGsonBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class ItemLoader extends JsonLoader {

	@NotNull
	private static final Gson GSON = SharedGsonBuilder.builder()
			.registerTypeAdapter(Item.class, Item.deserializer())
			.registerTypeAdapter(ItemConditions.class, ItemConditions.deserializer())
			.registerTypeAdapter(ItemConditionsBlockedIf.class, ItemConditionsBlockedIf.deserializer())
			.registerTypeAdapter(ItemEffectsEntry.class, ItemEffectsEntry.deserializer())
			.registerTypeAdapter(ItemEffectsEntryParameters.class, ItemEffectsEntryParameters.deserializer())
			.create();

	@NotNull
	public static CompletableFuture<Map<String, ItemCache>> load(@NotNull Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.ITEMS_PATH,
				ItemLoader::bake);
	}

	@NotNull
	private static ItemCache bake(@NotNull String pResourceName, @NotNull JsonObject pJsonObject) {
		Item moves = GSON.fromJson(pJsonObject, Item.class);
		return CacheFactory.constructWithFactory(ItemCacheFactory.INSTANCE, moves);
	}
}
