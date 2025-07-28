/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.dragons.DragonsCache;
import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.dragons.Dragons;
import com.mealam.showdown.loader.json.deserialize.dragons.Stats;
import com.mealam.showdown.loader.json.deserialize.moves.Effect;
import com.mealam.showdown.loader.json.deserialize.moves.EffectTarget;
import com.mealam.showdown.loader.json.deserialize.moves.Moves;
import com.mealam.showdown.loader.json.moves.MovesCacheFactory;
import com.mealam.showdown.utils.json.GsonExtensionsKt;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.json.GsonBuilderUtils;

public class JsonLoader {

	private static final Gson DRAGONS_GSON = GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays().setPrettyPrinting().setLenient()
			.registerTypeAdapter(Dragons.class, Dragons.deserializer())
			.registerTypeAdapter(Stats.class, Stats.deserializer())
			.create();

	private static final Gson MOVES_GSON = GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays().setPrettyPrinting().setLenient()
			.registerTypeAdapter(Moves.class, Moves.deserializer())
			.registerTypeAdapter(Effect.class, Effect.deserializer())
			.registerTypeAdapter(EffectTarget.class, EffectTarget.deserializer())
			.create();

	protected static CompletableFuture<Map<String, DragonsCache>> loadStaticDragons(Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.DRAGONS_PATH,
				JsonLoader::bakeDragons);
	}

	protected static CompletableFuture<Map<String, MovesCache>> loadStaticMoves(Executor pBackgroundExecutor) {
		return bakeGeneral(
				pBackgroundExecutor,
				Constants.Loader.MOVES_PATH,
				JsonLoader::bakeMoves);
	}

	protected static <T> CompletableFuture<Map<String, T>> bakeGeneral(
			Executor pBackgroundExecutor,
			String pPath,
			BiFunction<String, JsonObject, T> pFactory) {
		return bakeJsonResources(
				pBackgroundExecutor,
				pPath,
				pFactory,
				ex -> {
					Logger.log(LogLevel.ERROR, "Exception while baking " + pPath + ": " + ex.getMessage());
					return null;
				}).whenComplete((result, ex) -> {
					if (ex != null) {
						Logger.log(LogLevel.ERROR, "Failed to load static " + pPath + ": " + ex.getMessage());
					} else {
						Logger.log(LogLevel.INFO, "Successfully loaded static " + pPath + ". Count: " + (result != null ? result.size() : 0));
					}
				});
	}

	protected static <BAKED> CompletableFuture<Map<String, BAKED>> bakeJsonResources(
			Executor pBackgroundExecutor,
			String pAssetPath,
			BiFunction<String, JsonObject, BAKED> pElementFactory,
			Function<Throwable, BAKED> pExceptionalFactory) {
		Logger.log(LogLevel.INFO, "Baking JSON resources from: " + pAssetPath);
		return loadResources(pBackgroundExecutor, pAssetPath, "json")
				.thenCompose(resources -> {
					Logger.log(LogLevel.INFO, "Found " + resources.size() + " JSON resources in: " + pAssetPath);
					List<CompletableFuture<Pair<String, BAKED>>> tasks = new ObjectArrayList<>(resources.size());
					resources.forEach(pair -> tasks.add(
							CompletableFuture.supplyAsync(() -> {
								try {
									String key = cleanFileName(pair.left());
									return Pair.of(key, pElementFactory.apply(pair.left(), pair.right()));
								} catch (Exception ex) {
									Logger.log(LogLevel.ERROR, "Error processing resource " + pair.left() + ": " + ex.getMessage());
									throw ex;
								}
							}, pBackgroundExecutor).exceptionally(ex -> {
								Logger.log(LogLevel.ERROR, "Exceptionally handled resource: " + pair.left() + " - " + ex.getMessage());
								return Pair.of(pair.left(), pExceptionalFactory.apply(ex));
							})));
					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
							.thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull)
									.collect(Collectors.toMap(Pair::left, Pair::right)));
				});
	}

	protected static CompletableFuture<List<Pair<String, JsonObject>>> loadResources(
			Executor pBackgroundExecutor,
			String pAssetPath,
			String pFileType) {
		Logger.log(LogLevel.INFO, "Loading resources from: " + pAssetPath + " with file type: " + pFileType);
		return CompletableFuture.supplyAsync(() -> {
			try {
				PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				String pattern = "classpath*:" + pAssetPath + "/**/*." + pFileType;
				Resource[] resources = resolver.getResources(pattern);

				List<Pair<String, Resource>> files = new ObjectArrayList<>();
				for (Resource resource : resources) {
					if (resource.exists() && resource.isReadable()) {
						String filename = resource.getFilename();
						files.add(Pair.of(filename, resource));
					}
				}
				return files;
			} catch (IOException pIoException) {
				throw new RuntimeException("Failed to list resources in " + pAssetPath, pIoException);
			}
		}, pBackgroundExecutor).thenCompose(files -> {
			List<CompletableFuture<Pair<String, JsonObject>>> tasks = new ObjectArrayList<>(files.size());
			files.forEach(pair -> tasks.add(
					CompletableFuture.supplyAsync(() -> {
						try (Reader reader = new java.io.InputStreamReader(pair.right().getInputStream())) {
							return Pair.of(pair.left(), GsonExtensionsKt.parseJsonObject(reader, false));
						} catch (IOException e) {
							throw new RuntimeException("Failed to read resource: " + pair.left(), e);
						}
					}, pBackgroundExecutor)));
			return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
					.thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList());
		});
	}

	private static String cleanFileName(String pFileName) {
		String noPrefix = Constants.Loader.PREFIX_STRIPPER.matcher(pFileName).replaceFirst("");
		return Constants.Loader.SUFFIX_STRIPPER.matcher(noPrefix).replaceFirst("");
	}

	@NotNull
	protected static DragonsCache bakeDragons(String pResourceName, JsonObject pJsonObject) {
		return DRAGONS_GSON.fromJson(pJsonObject, DragonsCache.class);
	}

	@NotNull
	protected static MovesCache bakeMoves(String pResourceName, JsonObject pJsonObject) {
		Moves moves = MOVES_GSON.fromJson(pJsonObject, Moves.class);

		return CacheFactory.constructWithFactory(MovesCacheFactory.INSTANCE, moves);
	}
}
