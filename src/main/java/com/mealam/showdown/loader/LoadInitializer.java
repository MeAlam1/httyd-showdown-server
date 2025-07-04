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
