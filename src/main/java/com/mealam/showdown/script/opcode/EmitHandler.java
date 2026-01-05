/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.opcode;

import com.mealam.showdown.script.core.ExecutionResult;
import com.mealam.showdown.script.core.Step;
import com.mealam.showdown.script.runtime.EmittedEvent;
import com.mealam.showdown.script.runtime.ExecutionContext;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Emits an event with an optional payload.
 * <p>
 * Required args:
 * - eventId (String): the event identifier
 * <p>
 * Optional args:
 * - payload (Map): event data (will be defensively copied)
 */
public final class EmitHandler implements OpcodeHandler {

	@NotNull
	public static final EmitHandler INSTANCE = new EmitHandler();

	@NotNull
	public static final String ARG_EVENT_ID = "eventId";
	@NotNull
	public static final String ARG_PAYLOAD = "payload";

	private EmitHandler() {}

	@Override
	public @NotNull ExecutionResult execute(@NotNull ExecutionContext pContext, @NotNull Step pStep) {
		Map<String, Object> args = pStep.args();

		String eventId = extractEventId(args);
		if (eventId == null) {
			return ExecutionResult.skipped("EMIT: missing or invalid eventId");
		}

		Map<String, Object> payload = extractPayload(args);
		if (payload == null) {
			return ExecutionResult.skipped("EMIT: payload must be a Map");
		}

		// Defensive copy handed to context
		pContext.emit(new EmittedEvent(eventId, payload));
		return ExecutionResult.executed("Emitted: " + eventId);
	}

	@Nullable
	private static String extractEventId(@NotNull Map<String, Object> pArguments) {
		Object value = pArguments.get(ARG_EVENT_ID);
		if (value instanceof String s && !s.isBlank()) {
			return s;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private static Map<String, Object> extractPayload(@NotNull Map<String, Object> pArguments) {
		Object value = pArguments.get(ARG_PAYLOAD);
		if (value == null) {
			return Map.of();
		}
		if (value instanceof Map<?, ?> m) {
			Map<String, Object> cast = (Map<String, Object>) m;
			return Map.copyOf(cast);
		}
		return null;
	}
}
