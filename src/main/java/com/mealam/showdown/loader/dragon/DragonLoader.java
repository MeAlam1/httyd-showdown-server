/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.dragon;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.dragon.DragonCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.JsonLoader;
import com.mealam.showdown.loader.json.deserialize.dragon.*;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.*;
import com.mealam.showdown.utils.json.SharedGsonBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class DragonLoader extends JsonLoader {

	@NotNull
	private static final Gson DRAGONS_GSON = SharedGsonBuilder.builder()
			.registerTypeAdapter(DragonAbilities.class, DragonAbilities.deserializer())
			.registerTypeAdapter(DragonAppearances.class, DragonAppearances.deserializer())
			.registerTypeAdapter(DragonBiology.class, DragonBiology.deserializer())
			.registerTypeAdapter(DragonClassification.class, DragonClassification.deserializer())
			.registerTypeAdapter(Dragon.class, Dragon.deserializer())
			.registerTypeAdapter(DragonStats.class, DragonStats.deserializer())
			.registerTypeAdapter(DragonExternalStats.class, DragonExternalStats.deserializer())
			.registerTypeAdapter(DragonExternalValues.class, DragonExternalValues.deserializer())
			.registerTypeAdapter(DragonInternalStats.class, DragonInternalStats.deserializer())
			.registerTypeAdapter(DragonMedia.class, DragonMedia.deserializer())
			.registerTypeAdapter(DragonMetadata.class, DragonMetadata.deserializer())
			.registerTypeAdapter(DragonPhysicalStats.class, DragonPhysicalStats.deserializer())
			.create();

	@NotNull
	public static CompletableFuture<Map<String, DragonCache>> load(@NotNull Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.DRAGONS_PATH,
				DragonLoader::bake);
	}

	@NotNull
	private static DragonCache bake(@NotNull String pResourceName, @NotNull JsonObject pJsonObject) {
		Dragon dragons = DRAGONS_GSON.fromJson(pJsonObject, Dragon.class);
		return CacheFactory.constructWithFactory(DragonCacheFactory.INSTANCE, dragons);
	}
}
