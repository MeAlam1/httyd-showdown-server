/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {

	@Bean("backgroundTaskExecutor")
	public ThreadPoolTaskExecutor backgroundTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(16);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("BackgroundExecutor-");
		executor.initialize();
		return executor;
	}

	@Bean("serverTaskExecutor")
	public ThreadPoolTaskExecutor serverTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(16);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("ServerExecutor-");
		executor.initialize();
		return executor;
	}
}
