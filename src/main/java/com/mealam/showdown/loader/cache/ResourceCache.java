package com.mealam.showdown.loader.cache;

import com.mealam.showdown.loader.JsonLoader;
import com.mealam.showdown.loader.cache.dragons.DragonsCache;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceCache extends JsonLoader {
	private static Map<String, DragonsCache> DRAGONS = Collections.emptyMap();

	public static Map<String, DragonsCache> getDragons() {
		return DRAGONS;
	}

	public static CompletableFuture<Void> reload(
			Executor pBackgroundExecutor,
			Executor pServerExecutor) {
		clearCaches();

		CompletableFuture<Map<String, DragonsCache>> dragons = loadStaticDragons(pBackgroundExecutor);

		return CompletableFuture.allOf(dragons)
				.thenRunAsync(() -> {
					ResourceCache.DRAGONS = dragons.join();

					Logger.log(LogLevel.SUCCESS, "Dragons Cache: " + ResourceCache.DRAGONS);
				}, pServerExecutor);
	}

	private static void clearCaches() {
		ResourceCache.DRAGONS = Collections.emptyMap();
	}
}
