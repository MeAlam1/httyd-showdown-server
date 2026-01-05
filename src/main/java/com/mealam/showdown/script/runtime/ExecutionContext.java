/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.runtime;

import java.util.Map;
import java.util.random.RandomGenerator;

import org.jetbrains.annotations.NotNull;

/**
 * Runtime context for script execution.
 * <p>
 * Provides access to variables, RNG, and a generic event emission hook.
 */
public interface ExecutionContext {

	@NotNull
	RandomGenerator rng();

	@NotNull
	Map<String, Object> variables();

	default void emit(@NotNull EmittedEvent pEvent) {
		// No-op by default
	}
}
