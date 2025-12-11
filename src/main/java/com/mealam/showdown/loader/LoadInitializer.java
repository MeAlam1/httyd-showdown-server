/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader;

import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadInitializer {

	private static final Executor backgroundExecutor = Executors.newFixedThreadPool(2);
	private static final Executor serverExecutor = Executors.newSingleThreadExecutor();
	
	public static void init() {
		ResourceCache.reload(backgroundExecutor, serverExecutor)
				.thenRun(() -> Logger.log(LogLevel.INFO, "Resource cache loaded successfully."));
	}
}
