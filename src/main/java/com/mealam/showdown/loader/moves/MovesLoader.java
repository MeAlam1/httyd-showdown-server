/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.moves;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.JsonLoader;
import com.mealam.showdown.loader.json.deserialize.moves.Effect;
import com.mealam.showdown.loader.json.deserialize.moves.EffectTarget;
import com.mealam.showdown.loader.json.deserialize.moves.Moves;
import com.mealam.showdown.utils.json.SharedGsonBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class MovesLoader extends JsonLoader {

	@NotNull
	private static final Gson GSON = SharedGsonBuilder.builder()
			.registerTypeAdapter(Moves.class, Moves.deserializer())
			.registerTypeAdapter(Effect.class, Effect.deserializer())
			.registerTypeAdapter(EffectTarget.class, EffectTarget.deserializer())
			.create();

	@NotNull
	public static CompletableFuture<Map<String, MovesCache>> load(@NotNull Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.MOVES_PATH,
				MovesLoader::bake);
	}

	@NotNull
	private static MovesCache bake(@NotNull String pResourceName, @NotNull JsonObject pJsonObject) {
		Moves moves = GSON.fromJson(pJsonObject, Moves.class);
		return CacheFactory.constructWithFactory(MovesCacheFactory.INSTANCE, moves);
	}
}
