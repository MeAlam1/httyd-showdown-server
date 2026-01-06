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
 * Handler for the EMIT opcode.
 *
 * <p>This handler extracts a required string argument named {@code eventId} and an optional
 * {@code payload} map from the step's arguments. If {@code eventId} is missing or invalid
 * the step is skipped. If {@code payload} is present but not a Map the step is skipped.
 * On success an {@link EmittedEvent} is emitted via the supplied {@link ExecutionContext}.</p>
 *
 * <p>Argument contract:
 * - Required: {@link #ARG_EVENT_ID} (String, non-blank)
 * - Optional: {@link #ARG_PAYLOAD} (Map) — when absent an empty immutable map is used</p>
 */
public final class EmitHandler implements OpcodeHandler {

	@NotNull
	public static final EmitHandler INSTANCE = new EmitHandler();

	@NotNull
	public static final String ARG_EVENT_ID = "eventId";
	@NotNull
	public static final String ARG_PAYLOAD = "payload";

	private EmitHandler() {}

	/**
	 * Executes the EMIT opcode:
	 * - extracts and validates the required string {@code eventId}
	 * - extracts an optional payload map (empty map if absent)
	 * - emits an {@link EmittedEvent} via the provided {@link ExecutionContext}
	 * <p>
	 * Returns skipped results for missing/invalid arguments, executed result on success.
	 */
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

		// Defensive copy is performed by EmittedEvent
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

	/**
	 * Extracts the payload argument:
	 * - if absent -> returns empty map
	 * - if a Map -> returns it as-is (EmittedEvent will defensively copy)
	 * - otherwise -> returns null to indicate invalid type
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	private static Map<String, Object> extractPayload(@NotNull Map<String, Object> pArguments) {
		Object value = pArguments.get(ARG_PAYLOAD);
		if (value == null) {
			return Map.of();
		}
		if (value instanceof Map<?, ?> m) {
			// Return as-is; EmittedEvent will defensively copy to ensure immutability.
			return (Map<String, Object>) m;
		}
		return null;
	}
}
