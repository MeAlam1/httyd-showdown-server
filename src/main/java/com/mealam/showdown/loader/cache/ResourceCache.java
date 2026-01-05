/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache;

import com.mealam.showdown.loader.ability.AbilityLoader;
import com.mealam.showdown.loader.cache.ability.AbilityCache;
import com.mealam.showdown.loader.cache.dragon.DragonCache;
import com.mealam.showdown.loader.cache.item.ItemCache;
import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.loader.cache.nature.NatureCache;
import com.mealam.showdown.loader.dragon.DragonLoader;
import com.mealam.showdown.loader.item.ItemLoader;
import com.mealam.showdown.loader.json.JsonLoader;
import com.mealam.showdown.loader.moves.MovesLoader;
import com.mealam.showdown.loader.nature.NatureLoader;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceCache extends JsonLoader {

	private static volatile Map<String, DragonCache> DRAGONS = Collections.emptyMap();
	private static volatile Map<String, MovesCache> MOVES = Collections.emptyMap();
	private static volatile Map<String, NatureCache> NATURES = Collections.emptyMap();
	private static volatile Map<String, ItemCache> ITEMS = Collections.emptyMap();
	private static volatile Map<String, AbilityCache> ABILITIES = Collections.emptyMap();

	public static Map<String, DragonCache> getDragons() {
		return DRAGONS;
	}

	public static Map<String, MovesCache> getMoves() {
		return MOVES;
	}

	public static Map<String, NatureCache> getNatures() {
		return NATURES;
	}

	public static Map<String, ItemCache> getItems() {
		return ITEMS;
	}

	public static Map<String, AbilityCache> getAbilities() {
		return ABILITIES;
	}

	public static CompletableFuture<Void> reload(Executor pBackgroundExecutor, Executor pServerExecutor) {
		clearCaches();

		CompletableFuture<Map<String, DragonCache>> dragons = DragonLoader.load(pBackgroundExecutor);
		CompletableFuture<Map<String, MovesCache>> moves = MovesLoader.load(pBackgroundExecutor);
		CompletableFuture<Map<String, NatureCache>> natures = NatureLoader.load(pBackgroundExecutor);
		CompletableFuture<Map<String, ItemCache>> items = ItemLoader.load(pBackgroundExecutor);
		CompletableFuture<Map<String, AbilityCache>> abilities = AbilityLoader.load(pBackgroundExecutor);

		return CompletableFuture.allOf(dragons, moves, natures, items, abilities)
				.thenRunAsync(() -> {
					DRAGONS = defaultIfNull(dragons.getNow(null));
					MOVES = defaultIfNull(moves.getNow(null));
					NATURES = defaultIfNull(natures.getNow(null));
					ITEMS = defaultIfNull(items.getNow(null));
					ABILITIES = defaultIfNull(abilities.getNow(null));

					BaseLogger.log(BaseLogLevel.SUCCESS, "Caches loaded - dragons=" + DRAGONS.size()
							+ ", moves=" + MOVES.size()
							+ ", natures=" + NATURES.size()
							+ ", items=" + ITEMS.size()
							+ ", abilities=" + ABILITIES.size());
				}, pServerExecutor);
	}

	private static <K, V> Map<K, V> defaultIfNull(Map<K, V> pMap) {
		return pMap != null ? pMap : Collections.emptyMap();
	}

	private static void clearCaches() {
		DRAGONS = Collections.emptyMap();
		MOVES = Collections.emptyMap();
		NATURES = Collections.emptyMap();
		ITEMS = Collections.emptyMap();
		ABILITIES = Collections.emptyMap();
	}
}
