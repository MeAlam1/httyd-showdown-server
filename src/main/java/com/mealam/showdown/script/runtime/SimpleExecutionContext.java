/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;
import org.jetbrains.annotations.NotNull;

public final class SimpleExecutionContext implements ExecutionContext {

	private final RandomGenerator rng = RandomGenerator.getDefault();
	private final Map<String, Object> variables = new HashMap<>();
	private final List<EmittedEvent> emitted = new ArrayList<>();

	public SimpleExecutionContext() {}

	@Override
	public @NotNull RandomGenerator rng() {
		return rng;
	}

	@Override
	public @NotNull Map<String, Object> variables() {
		return variables;
	}

	@Override
	public void emit(@NotNull EmittedEvent pEvent) {
		emitted.add(pEvent);
	}

	public List<EmittedEvent> emittedEvents() {
		return List.copyOf(emitted);
	}
}
