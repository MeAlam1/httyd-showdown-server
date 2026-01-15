/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.ability;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.ability.AbilityCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.JsonLoader;
import com.mealam.showdown.loader.json.deserialize.ability.*;
import com.mealam.showdown.utils.json.SharedGsonBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.jetbrains.annotations.NotNull;

public class AbilityLoader extends JsonLoader {

	@NotNull
	private static final Gson ABILITY_GSON = SharedGsonBuilder.builder()
			.registerTypeAdapter(Ability.class, Ability.deserializer())
			.registerTypeAdapter(AbilityEvent.class, AbilityEvent.deserializer())
			.registerTypeAdapter(AbilityRequirements.class, AbilityRequirements.deserializer())
			.create();

	@NotNull
	public static CompletableFuture<Map<String, AbilityCache>> load(@NotNull Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.ABILITIES_PATH,
				AbilityLoader::bake);
	}

	@NotNull
	private static AbilityCache bake(@NotNull String pResourceName, @NotNull JsonObject pJsonObject) {
		Ability moves = ABILITY_GSON.fromJson(pJsonObject, Ability.class);
		return CacheFactory.constructWithFactory(AbilityCacheFactory.INSTANCE, moves);
	}
}
