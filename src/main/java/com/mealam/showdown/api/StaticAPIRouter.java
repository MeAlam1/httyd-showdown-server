package com.mealam.showdown.api;

import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.utils.path.PathUtils;
import com.mealam.showdown.utils.resource.ResourceUtils;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class StaticAPIRouter {

	private static final String BASE_PATH = "static/api/";
	private static final String DRAGONS_PREFIX = "dragons/";
	private static final String MOVES_PREFIX = "moves/";

	private static final Executor backgroundExecutor = Executors.newFixedThreadPool(2);
	private static final Executor serverExecutor = Executors.newSingleThreadExecutor();
	private static volatile boolean initialized = false;

	public static void register(Javalin pApp) {
		ensureInitialized();

		pApp.get("/static/api", pContext -> {
			Map<String, Object> payload = buildGroupedIndexPayload();
			pContext.contentType("application/json");
			pContext.json(payload);
		});

		pApp.get("/static/api/<path>", pContext -> {
			String requestPath = pContext.pathParam("path");

			if (!PathUtils.isValidSegment(requestPath)) {
				pContext.status(HttpStatus.BAD_REQUEST).json(Map.of("error", "Invalid path segment", "path", requestPath));
				return;
			}

			String normalized = PathUtils.normalize(requestPath);
			String resourcePath = BASE_PATH + (normalized.endsWith(".json") ? normalized : normalized + ".json");

			String json = ResourceUtils.loadResource(resourcePath);

			if (json == null) {
				pContext.status(HttpStatus.NOT_FOUND).json(Map.of("error", "Resource not found", "resource", resourcePath));
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

	private static Map<String, Object> buildGroupedIndexPayload() {
		List<String> all = listApiResources();

		List<String> dragons = new ArrayList<>();
		List<String> moves = new ArrayList<>();

		for (String rel : all) {
			if (rel.startsWith(DRAGONS_PREFIX)) {
				dragons.add(rel.substring(DRAGONS_PREFIX.length()));
			} else if (rel.startsWith(MOVES_PREFIX)) {
				moves.add(rel.substring(MOVES_PREFIX.length()));
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
		List<String> results = new ArrayList<>();

		try {
			ClassLoader cl = StaticAPIRouter.class.getClassLoader();
			Enumeration<URL> urls = cl.getResources(BASE_PATH);

			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {
					File dir = new File(url.toURI());
					collectFromDirectory(dir, "", results);
				} else if ("jar".equals(protocol)) {
					collectFromJar(url, results);
				}
			}
		} catch (Exception ignored) {
			// If listing fails, return an empty list rather than exposing internals/errors.
		}

		results.sort(String::compareTo);
		return results;
	}

	private static void collectFromDirectory(File dir, String prefix, List<String> out) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) return;

		File[] files = dir.listFiles();
		if (files == null) return;

		for (File file : files) {
			String name = file.getName();
			String rel = prefix.isEmpty() ? name : prefix + "/" + name;

			if (file.isDirectory()) {
				collectFromDirectory(file, rel, out);
			} else if (name.endsWith(".json")) {
				out.add(rel);
			}
		}
	}

	private static void collectFromJar(URL baseUrl, List<String> out) {
		try {
			JarURLConnection conn = (JarURLConnection) baseUrl.openConnection();
			try (JarFile jar = conn.getJarFile()) {
				String baseEntry = conn.getEntryName();
				if (baseEntry == null) baseEntry = BASE_PATH;

				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String name = entry.getName();

					if (entry.isDirectory()) continue;
					if (!name.startsWith(baseEntry)) continue;
					if (!name.endsWith(".json")) continue;

					String rel = name.substring(baseEntry.length());
					if (rel.startsWith("/")) rel = rel.substring(1);
					if (!rel.isEmpty()) out.add(rel);
				}
			}
		} catch (ClassCastException e) {
			// Fallback for some classloaders: parse jar path manually
			try {
				String raw = baseUrl.toString();
				int sep = raw.indexOf("!/");
				if (sep < 0) return;

				String jarPart = raw.substring(0, sep);
				if (jarPart.startsWith("jar:")) jarPart = jarPart.substring(4);

				String decoded = URLDecoder.decode(jarPart, StandardCharsets.UTF_8);
				URI uri = URI.create(decoded);

				try (JarFile jar = new JarFile(new File(uri))) {
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();

						if (entry.isDirectory()) continue;
						if (!name.startsWith(BASE_PATH)) continue;
						if (!name.endsWith(".json")) continue;

						String rel = name.substring(BASE_PATH.length());
						if (!rel.isEmpty()) out.add(rel);
					}
				}
			} catch (Exception ignored) {
				// Ignore
			}
		} catch (Exception ignored) {
			// Ignore
		}
	}
}