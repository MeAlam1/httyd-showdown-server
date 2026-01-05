/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.nature;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.nature.NatureCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.JsonLoader;
import com.mealam.showdown.loader.json.deserialize.nature.*;
import com.mealam.showdown.utils.json.SharedGsonBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class NatureLoader extends JsonLoader {

	@NotNull
	private static final Gson GSON = SharedGsonBuilder.builder()
			.registerTypeAdapter(Nature.class, Nature.deserializer())
			.registerTypeAdapter(NatureStatMultipliers.class, NatureStatMultipliers.deserializer())
			.registerTypeAdapter(NatureGrowthCurves.class, NatureGrowthCurves.deserializer())
			.registerTypeAdapter(NaturePassiveEffectsEntry.class, NaturePassiveEffectsEntry.deserializer())
			.registerTypeAdapter(NaturePassiveEffectsEntryParameters.class, NaturePassiveEffectsEntryParameters.deserializer())
			.create();

	@NotNull
	public static CompletableFuture<Map<String, NatureCache>> load(@NotNull Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.NATURES_PATH,
				NatureLoader::bake);
	}

	@NotNull
	private static NatureCache bake(@NotNull String pResourceName, @NotNull JsonObject pJsonObject) {
		Nature moves = GSON.fromJson(pJsonObject, Nature.class);
		return CacheFactory.constructWithFactory(NatureCacheFactory.INSTANCE, moves);
	}
}
