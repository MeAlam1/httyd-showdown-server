/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.resource;

import com.mealam.showdown.Main;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceUtils {

	public static Map<String, String> loadResources(String pBasePath) {
		ClassLoader cl = Main.class.getClassLoader();
		String base = normalizeBasePath(pBasePath);

		List<String> relPaths = listResources(cl, base);
		Map<String, String> out = new LinkedHashMap<>(relPaths.size());

		for (String rel : relPaths) {
			String fullPath = base + rel;
			String content = loadResource(fullPath);
			if (content != null) out.put(rel, content);
		}

		return out;
	}

	public static String loadResource(String pPath) {
		try (InputStream in = Main.class.getClassLoader().getResourceAsStream(pPath)) {
			if (in == null) return null;
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}
	}

	public static List<String> listResources(ClassLoader pClassLoader, String pBasePath) {
		List<String> results = new ArrayList<>();
		if (pClassLoader == null || pBasePath == null || pBasePath.isBlank()) return results;

		String basePath = normalizeBasePath(pBasePath);

		try {
			Enumeration<URL> urls = pClassLoader.getResources(basePath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {
					File dir = new File(url.toURI());
					collectFromDirectory(dir, "", results);
				} else if ("jar".equals(protocol)) {
					collectFromJar(url, basePath, results);
				}
			}
		} catch (Exception ignored) {
			// Ignore.
		}

		results.sort(String::compareTo);
		return results;
	}

	public static List<String> listResourcesWithSuffix(String pBasePath, String pSuffix) {
		List<String> all = listResources(Main.class.getClassLoader(), pBasePath);
		if (pSuffix == null || pSuffix.isEmpty()) return all;

		List<String> filtered = new ArrayList<>(all.size());
		for (String rel : all) {
			if (rel.endsWith(pSuffix)) filtered.add(rel);
		}
		filtered.sort(String::compareTo);
		return filtered;
	}

	private static String normalizeBasePath(String pBasePath) {
		String base = pBasePath;
		if (base.startsWith("/")) base = base.substring(1);
		if (!base.endsWith("/")) base = base + "/";
		return base;
	}

	private static void collectFromDirectory(File pDir, String pPrefix, List<String> pOut) {
		if (pDir == null || !pDir.exists() || !pDir.isDirectory()) return;

		File[] files = pDir.listFiles();
		if (files == null) return;

		for (File file : files) {
			String name = file.getName();
			String rel = pPrefix.isEmpty() ? name : pPrefix + "/" + name;

			if (file.isDirectory()) {
				collectFromDirectory(file, rel, pOut);
			} else {
				pOut.add(rel);
			}
		}
	}

	private static void collectFromJar(URL pBaseUrl, String pBasePath, List<String> pOut) {
		try {
			JarURLConnection conn = (JarURLConnection) pBaseUrl.openConnection();
			try (JarFile jar = conn.getJarFile()) {
				String baseEntry = conn.getEntryName();
				String basePrefix = (baseEntry == null || baseEntry.isEmpty()) ? pBasePath : baseEntry;
				if (!basePrefix.endsWith("/")) basePrefix = basePrefix + "/";

				getElements(pOut, jar, basePrefix);
			}
		} catch (ClassCastException pClassCastException) {
			fallbackJarScan(pBaseUrl, pBasePath, pOut);
		} catch (Exception ignored) {
			// Ignore.
		}
	}

	private static void fallbackJarScan(URL pBaseUrl, String pBasePath, List<String> pOut) {
		try {
			String raw = pBaseUrl.toString();
			int sep = raw.indexOf("!/");
			if (sep < 0) return;

			String jarPart = raw.substring(0, sep);
			if (jarPart.startsWith("jar:")) jarPart = jarPart.substring(4);

			String decoded = URLDecoder.decode(jarPart, StandardCharsets.UTF_8);
			URI uri = URI.create(decoded);

			try (JarFile jar = new JarFile(new File(uri))) {
				getElements(pOut, jar, normalizeBasePath(pBasePath));
			}
		} catch (Exception ignored) {
			// Ignore.
		}
	}

	private static void getElements(List<String> pOut, JarFile pJar, String pBasePrefix) {
		Enumeration<JarEntry> entries = pJar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();

			if (entry.isDirectory()) continue;
			if (!name.startsWith(pBasePrefix)) continue;

			String rel = name.substring(pBasePrefix.length());
			if (!rel.isEmpty()) pOut.add(rel);
		}
	}
}
