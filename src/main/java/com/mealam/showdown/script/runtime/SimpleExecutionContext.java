/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.runtime;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

/**
 * Simple in-memory execution context that captures emitted events.
 */
public final class SimpleExecutionContext implements ExecutionContext {

	@NotNull
	private final RandomGenerator rng;
	@NotNull
	private final Map<String, Object> vars;
	@NotNull
	private final List<EmittedEvent> events = new ArrayList<>();

	public SimpleExecutionContext(@NotNull RandomGenerator pRng, @NotNull Map<String, Object> pVariables) {
		this.rng = pRng;
		this.vars = Map.copyOf(pVariables);
	}

	@Override
	public @NotNull RandomGenerator rng() {
		return rng;
	}

	@Override
	public @NotNull Map<String, Object> variables() {
		return vars;
	}

	@Override
	public void emit(@NotNull EmittedEvent pEvent) {
		events.add(pEvent);
	}

	@NotNull
	public List<EmittedEvent> events() {
		return List.copyOf(events);
	}

	public void clearEvents() {
		events.clear();
	}
}
