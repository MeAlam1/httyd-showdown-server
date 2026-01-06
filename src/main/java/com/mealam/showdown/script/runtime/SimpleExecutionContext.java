package com.mealam.showdown.script.runtime;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

public final class SimpleExecutionContext implements ExecutionContext {

	private final RandomGenerator rng = RandomGenerator.getDefault();
	private final Map<String, Object> variables = new HashMap<>();
	private final List<EmittedEvent> emitted = new ArrayList<>();

	public SimpleExecutionContext() {
	}

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