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
 * Represents the runtime environment used while executing script steps.
 *
 * <p>Implementations must provide:
 * - an RNG via {@link #rng()} for chance checks
 * - a variables map via {@link #variables()} that expression evaluators may read
 * - an {@link #emit(EmittedEvent)} hook that receives events produced by EMIT opcodes.</p>
 *
 * <p>The default {@link #emit(EmittedEvent)} implementation is a no-op. Consumers may
 * provide a context implementation that records or dispatches events as needed.</p>
 */
public interface ExecutionContext {

	@NotNull
	RandomGenerator rng();

	@NotNull
	Map<String, Object> variables();

	/**
	 * Hook for receiving emitted events. Default implementation is a no-op;
	 * callers can override to observe or handle events emitted by scripts.
	 *
	 * @param pEvent the event being emitted
	 */
	default void emit(@NotNull EmittedEvent pEvent) {
		// No-op by default
	}
}
