/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache;

import com.mealam.showdown.loader.JsonLoader;
import com.mealam.showdown.loader.cache.dragons.DragonsCache;
import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceCache extends JsonLoader {

	private static Map<String, DragonsCache> DRAGONS = Collections.emptyMap();
	private static Map<String, MovesCache> MOVES = Collections.emptyMap();

	public static Map<String, DragonsCache> getDragons() {
		return DRAGONS;
	}

	public static Map<String, MovesCache> getMoves() {
		return MOVES;
	}

	public static CompletableFuture<Void> reload(Executor pBackgroundExecutor, Executor pServerExecutor) {
		clearCaches();
		CompletableFuture<Map<String, DragonsCache>> dragons = loadStaticDragons(pBackgroundExecutor);
		CompletableFuture<Map<String, MovesCache>> moves = loadStaticMoves(pBackgroundExecutor);

		return CompletableFuture.allOf(dragons, moves)
				.thenRunAsync(() -> {
					ResourceCache.DRAGONS = dragons.join();
					ResourceCache.MOVES = moves.join();
					Logger.log(LogLevel.SUCCESS, "Dragons Cache: " + ResourceCache.DRAGONS);
					Logger.log(LogLevel.SUCCESS, "Moves Cache: " + ResourceCache.MOVES);
				}, pServerExecutor);
	}

	private static void clearCaches() {
		ResourceCache.DRAGONS = Collections.emptyMap();
		ResourceCache.MOVES = Collections.emptyMap();
	}
}
