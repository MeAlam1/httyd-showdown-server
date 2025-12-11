package com.mealam.showdown.api;

import com.mealam.showdown.utils.path.PathUtils;
import com.mealam.showdown.utils.resource.ResourceUtils;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticAPIRouter {

	private static final String BASE_PATH = "static/api/";
	private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

	public static void register(Javalin pApp) {
		pApp.get("/static/api/<path>", ctx -> {
			String requestPath = ctx.pathParam("path");

			if (!PathUtils.isValidSegment(requestPath)) {
				ctx.status(HttpStatus.BAD_REQUEST)
						.json(Map.of("error", "Invalid path segment", "path", requestPath));
				return;
			}

			String normalized = PathUtils.normalize(requestPath);
			String resourcePath = BASE_PATH + (normalized.endsWith(".json") ? normalized : normalized + ".json");

			//TODO: Combine with the Loader Package
			String json = CACHE.computeIfAbsent(resourcePath, ResourceUtils::loadResource);

			if (json == null) {
				ctx.status(HttpStatus.NOT_FOUND)
						.json(Map.of("error", "Resource not found", "resource", resourcePath));
				return;
			}

			ctx.contentType("application/json");
			ctx.result(json);
		});
	}
}
