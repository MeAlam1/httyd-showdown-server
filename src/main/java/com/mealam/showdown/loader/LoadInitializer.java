/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader;

import com.mealam.showdown.Constants;
import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LoadInitializer {

	@PostConstruct
	public void init() {
		ResourceCache.reload(Constants.BACKGROUND_EXECUTOR, Constants.SERVER_EXECUTOR)
				.thenRun(() -> Logger.log(LogLevel.INFO, "Resource cache loaded successfully."));
	}
}
