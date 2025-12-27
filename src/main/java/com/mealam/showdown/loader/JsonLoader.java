/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.dragon.DragonCache;
import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.common.Measurement;
import com.mealam.showdown.loader.json.deserialize.common.NamedResource;
import com.mealam.showdown.loader.json.deserialize.dragon.*;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.*;
import com.mealam.showdown.loader.json.deserialize.moves.Effect;
import com.mealam.showdown.loader.json.deserialize.moves.EffectTarget;
import com.mealam.showdown.loader.json.deserialize.moves.Moves;
import com.mealam.showdown.loader.json.dragon.DragonCacheFactory;
import com.mealam.showdown.loader.json.moves.MovesCacheFactory;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.eclipse.jetty.util.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonLoader {

	private static final Gson DRAGONS_GSON = new GsonBuilder().setPrettyPrinting().setLenient()
			.registerTypeAdapter(Abilities.class, Abilities.deserializer())
			.registerTypeAdapter(Appearances.class, Appearances.deserializer())
			.registerTypeAdapter(Biology.class, Biology.deserializer())
			.registerTypeAdapter(Classification.class, Classification.deserializer())
			.registerTypeAdapter(Dragon.class, Dragon.deserializer())
			.registerTypeAdapter(DragonStats.class, DragonStats.deserializer())
			.registerTypeAdapter(ExternalStats.class, ExternalStats.deserializer())
			.registerTypeAdapter(ExternalValues.class, ExternalValues.deserializer())
			.registerTypeAdapter(InternalStats.class, InternalStats.deserializer())
			.registerTypeAdapter(Measurement.class, Measurement.deserializer())
			.registerTypeAdapter(Media.class, Media.deserializer())
			.registerTypeAdapter(Metadata.class, Metadata.deserializer())
			.registerTypeAdapter(NamedResource.class, NamedResource.deserializer())
			.registerTypeAdapter(PhysicalStats.class, PhysicalStats.deserializer())
			.create();

	private static final Gson MOVES_GSON = new GsonBuilder().setPrettyPrinting().setLenient()
			.registerTypeAdapter(Moves.class, Moves.deserializer())
			.registerTypeAdapter(Effect.class, Effect.deserializer())
			.registerTypeAdapter(EffectTarget.class, EffectTarget.deserializer())
			.create();

	protected static CompletableFuture<Map<String, DragonCache>> loadStaticDragons(Executor pBackgroundExecutor) {
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
					BaseLogger.log(BaseLogLevel.ERROR, "Exception while baking " + pPath + ": " + ex.getMessage());
					return null;
				}).whenComplete((result, ex) -> {
			if (ex != null) {
				BaseLogger.log(BaseLogLevel.ERROR, "Failed to load static " + pPath + ": " + ex.getMessage());
			} else {
				BaseLogger.log(BaseLogLevel.INFO, "Successfully loaded static " + pPath + ". Count: " + (result != null ? result.size() : 0));
			}
		});
	}

	protected static <BAKED> CompletableFuture<Map<String, BAKED>> bakeJsonResources(
			Executor pBackgroundExecutor,
			String pAssetPath,
			BiFunction<String, JsonObject, BAKED> pElementFactory,
			Function<Throwable, BAKED> pExceptionalFactory) {
		BaseLogger.log(BaseLogLevel.INFO, "Baking JSON resources from: " + pAssetPath);
		return loadResources(pBackgroundExecutor, pAssetPath, "json")
				.thenCompose(resources -> {
					BaseLogger.log(BaseLogLevel.INFO, "Found " + resources.size() + " JSON resources in: " + pAssetPath);
					List<CompletableFuture<Pair<String, BAKED>>> tasks = new ObjectArrayList<>(resources.size());
					resources.forEach(pair -> tasks.add(
							CompletableFuture.supplyAsync(() -> {
								try {
									String key = cleanFileName(pair.left());
									return Pair.of(key, pElementFactory.apply(pair.left(), pair.right()));
								} catch (Exception ex) {
									BaseLogger.log(BaseLogLevel.ERROR, "Error processing resource " + pair.left() + ": " + ex.getMessage());
									throw ex;
								}
							}, pBackgroundExecutor).exceptionally(ex -> {
								BaseLogger.log(BaseLogLevel.ERROR, "Exceptionally handled resource: " + pair.left() + " - " + ex.getMessage());
								BAKED fallback = pExceptionalFactory.apply(ex);
								return (fallback != null) ? Pair.of(pair.left(), fallback) : null;
							})));
					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
							.thenApply(ignored -> tasks.stream()
									.map(CompletableFuture::join)
									.filter(Objects::nonNull)
									.collect(Collectors.toMap(Pair::left, Pair::right)));
				});
	}

	protected static CompletableFuture<List<Pair<String, JsonObject>>> loadResources(
			Executor pBackgroundExecutor,
			String pAssetPath,
			String pFileType) {
		BaseLogger.log(BaseLogLevel.INFO, "Loading resources from: " + pAssetPath + " with file type: " + pFileType);
		return CompletableFuture.supplyAsync(() -> {
			List<Pair<String, Resource>> files = new ObjectArrayList<>();
			String base = pAssetPath.endsWith("/") ? pAssetPath : pAssetPath + "/";
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			try {
				URL baseUrl = cl.getResource(base);
				if (baseUrl == null) {
					BaseLogger.log(BaseLogLevel.WARNING, "Resource base not found on classpath: " + base);
					return files;
				}
				String protocol = baseUrl.getProtocol();
				if ("file".equals(protocol)) {
					Path baseDir = Paths.get(baseUrl.toURI());
					try (Stream<Path> stream = Files.walk(baseDir)) {
						stream.filter(Files::isRegularFile)
								.filter(p -> p.getFileName().toString().endsWith("." + pFileType))
								.forEach(p -> {
									Path rel = baseDir.relativize(p);
									String relUnix = rel.toString().replace('\\', '/');
									try {
										Resource res = Resource.newResource(p.toUri());
										files.add(Pair.of(relUnix, res));
									} catch (Exception e) {
										throw new RuntimeException("Failed to create Resource for: " + p, e);
									}
								});
					}
				} else if ("jar".equals(protocol)) {
					JarURLConnection conn = (JarURLConnection) baseUrl.openConnection();
					try (JarFile jar = conn.getJarFile()) {
						jar.stream()
								.filter(e -> !e.isDirectory())
								.filter(e -> e.getName().startsWith(base))
								.filter(e -> e.getName().endsWith("." + pFileType))
								.forEach(e -> {
									String rel = e.getName().substring(base.length());
									URL entryUrl = cl.getResource(e.getName());
									if (entryUrl != null) {
										try {
											Resource res = Resource.newResource(entryUrl);
											files.add(Pair.of(rel, res));
										} catch (Exception ex) {
											throw new RuntimeException("Failed to create Resource for JAR entry: " + e.getName(), ex);
										}
									}
								});
					}
				} else {
					Resource baseRes = Resource.newClassPathResource(base);
					if (baseRes != null && baseRes.exists()) {
						addResourcesRecursively(baseRes, "", pFileType, files);
					} else {
						BaseLogger.log(BaseLogLevel.WARNING, "Unsupported protocol " + protocol + " for: " + base);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed scanning resources under: " + base, e);
			}
			return files;
		}, pBackgroundExecutor).thenCompose(files -> {
			List<CompletableFuture<Pair<String, JsonObject>>> tasks = new ObjectArrayList<>(files.size());
			files.forEach(pair -> tasks.add(
					CompletableFuture.supplyAsync(() -> {
						try (Reader reader = new java.io.InputStreamReader(pair.right().getInputStream())) {
							return Pair.of(pair.left(), GsonHelper.parse(reader, false));
						} catch (IOException e) {
							throw new RuntimeException("Failed to read resource: " + pair.left(), e);
						}
					}, pBackgroundExecutor)));
			return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
					.thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList());
		});
	}

	private static void addResourcesRecursively(Resource base, String relPrefix, String fileType, List<Pair<String, Resource>> out) {
		try {
			for (String child : base.list()) {
				Resource childRes = base.addPath(child);
				String rel = relPrefix.isEmpty() ? child : relPrefix + child;
				if (childRes.isDirectory()) {
					String newPrefix = rel.endsWith("/") ? rel : rel + "/";
					addResourcesRecursively(childRes, newPrefix, fileType, out);
				} else if (rel.endsWith("." + fileType)) {
					out.add(Pair.of(rel, childRes));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to traverse resources at: " + base, e);
		}
	}

	private static String cleanFileName(String pFileName) {
		String noPrefix = Constants.Loader.PREFIX_STRIPPER.matcher(pFileName).replaceFirst("");
		return Constants.Loader.SUFFIX_STRIPPER.matcher(noPrefix).replaceFirst("");
	}

	@NotNull
	protected static DragonCache bakeDragons(String pResourceName, JsonObject pJsonObject) {
		Dragon dragons = DRAGONS_GSON.fromJson(pJsonObject, Dragon.class);
		return CacheFactory.constructWithFactory(DragonCacheFactory.INSTANCE, dragons);
	}

	@NotNull
	protected static MovesCache bakeMoves(String pResourceName, JsonObject pJsonObject) {
		Moves moves = MOVES_GSON.fromJson(pJsonObject, Moves.class);
		return CacheFactory.constructWithFactory(MovesCacheFactory.INSTANCE, moves);
	}
}