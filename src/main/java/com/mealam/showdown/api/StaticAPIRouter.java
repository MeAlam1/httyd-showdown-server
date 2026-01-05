/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.api;

import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.utils.ThreadFactoryUtils;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.path.PathUtils;
import com.mealam.showdown.utils.resource.ResourceUtils;
import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StaticAPIRouter {

	private static final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(2, ThreadFactoryUtils.daemonThreads("static-api-bg"));
	private static final ExecutorService serverExecutor = Executors.newSingleThreadExecutor(ThreadFactoryUtils.daemonThreads("static-api-srv"));

	private static volatile boolean initialized = false;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			backgroundExecutor.shutdown();
			serverExecutor.shutdown();
		}, "static-api-shutdown"));
	}

	public static void register(Javalin pApp) {
		ensureInitialized();

		pApp.get("/static/api", pContext -> {
			Map<String, Object> payload = buildGroupedIndexPayload();
			ResponseUtils.ok(pContext, payload);
		});

		pApp.get("/static/api/<path>", pContext -> {
			String requestPath = pContext.pathParam("path");

			if (!isValidMultiSegmentPath(requestPath)) {
				ResponseUtils.badRequest(pContext, "Invalid path", "STATIC_API_INVALID_PATH");
				return;
			}

			String normalized = PathUtils.normalize(requestPath);
			String resourcePath = Constants.Loader.BASE_PATH + (normalized.endsWith(".json") ? normalized : normalized + ".json");

			String json = ResourceUtils.loadResource(resourcePath);

			if (json == null) {
				ResponseUtils.notFound(pContext, "Resource not found", "STATIC_API_RESOURCE_NOT_FOUND");
				return;
			}

			pContext.contentType(Constants.Loader.JSON_CONTENT_TYPE);
			pContext.result(json);
		});
	}

	private static void ensureInitialized() {
		if (initialized) return;

		synchronized (StaticAPIRouter.class) {
			if (initialized) return;

			ResourceCache.reload(backgroundExecutor, serverExecutor).join();
			initialized = true;
		}
	}

	private static Map<String, Object> buildGroupedIndexPayload() {
		List<String> all = listApiResources();

		List<String> dragons = new ArrayList<>();
		List<String> moves = new ArrayList<>();

		for (String rel : all) {
			if (rel.startsWith(Constants.Loader.DRAGONS_PREFIX)) {
				dragons.add(rel.substring(Constants.Loader.DRAGONS_PREFIX.length()));
			} else if (rel.startsWith(Constants.Loader.MOVES_PREFIX)) {
				moves.add(rel.substring(Constants.Loader.MOVES_PREFIX.length()));
			}
		}

		dragons.sort(String::compareTo);
		moves.sort(String::compareTo);

		Map<String, Object> payload = new HashMap<>();
		payload.put("base", "/static/api");
		payload.put("dragonsBase", "/static/api/dragons");
		payload.put("movesBase", "/static/api/moves");
		payload.put("dragonsCount", dragons.size());
		payload.put("movesCount", moves.size());
		payload.put("dragons", dragons);
		payload.put("moves", moves);
		return payload;
	}

	private static List<String> listApiResources() {
		List<String> files = ResourceUtils.listResourcesWithSuffix(Constants.Loader.BASE_PATH, ".json");
		List<String> rel = new ArrayList<>(files.size());

		for (String path : files) {
			if (path.endsWith(".json")) rel.add(path);
		}

		rel.sort(String::compareTo);
		return rel;
	}

	private static boolean isValidMultiSegmentPath(String pPath) {
		if (pPath == null || pPath.isBlank()) return false;

		if (pPath.contains("..") || pPath.contains("\\") || pPath.startsWith("/") || pPath.endsWith("/")) return false;

		String[] parts = pPath.split("/");
		for (String part : parts) {
			if (part.isEmpty()) return false;
			if (!PathUtils.isValidSegment(part)) return false;
		}
		return true;
	}
}
