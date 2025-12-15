package com.mealam.showdown.api;

import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.utils.path.PathUtils;
import com.mealam.showdown.utils.resource.ResourceUtils;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StaticAPIRouter {

	private static final String BASE_PATH = "static/api/";
	private static final Executor backgroundExecutor = Executors.newFixedThreadPool(2);
	private static final Executor serverExecutor = Executors.newSingleThreadExecutor();
	private static volatile boolean initialized = false;

	public static void register(Javalin pApp) {
		ensureInitialized();

		pApp.get("/static/api/<path>", pContext -> {
			String requestPath = pContext.pathParam("path");

			if (!PathUtils.isValidSegment(requestPath)) {
				pContext.status(HttpStatus.BAD_REQUEST)
						.json(java.util.Map.of("error", "Invalid path segment", "path", requestPath));
				return;
			}

			String normalized = PathUtils.normalize(requestPath);
			String resourcePath = BASE_PATH + (normalized.endsWith(".json") ? normalized : normalized + ".json");

			String json = ResourceUtils.loadResource(resourcePath);

			if (json == null) {
				pContext.status(HttpStatus.NOT_FOUND)
						.json(java.util.Map.of("error", "Resource not found", "resource", resourcePath));
				return;
			}

			pContext.contentType("application/json");
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
}