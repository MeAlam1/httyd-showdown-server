/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.opcode;

import com.mealam.showdown.script.core.ExecutionResult;
import com.mealam.showdown.script.core.Opcode;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;

/**
 * Thread-safe registry that maps {@link Opcode} values to their {@link OpcodeHandler} implementations.
 *
 * <p>When a handler is not found the registry returns a safe default handler that returns
 * a skipped {@link ExecutionResult} instead of throwing. This design keeps script execution
 * robust in the presence of missing or unregistered opcodes.</p>
 */
public final class OpcodeRegistry {

	@NotNull
	private final ConcurrentMap<Opcode, OpcodeHandler> handlers = new ConcurrentHashMap<>();

	@NotNull
	public OpcodeRegistry register(@NotNull Opcode pOpcode, @NotNull OpcodeHandler pHandler) {
		handlers.put(pOpcode, pHandler);
		return this;
	}

	/**
	 * Returns the handler for the opcode, or a default handler that returns
	 * a skipped ExecutionResult instead of throwing.
	 * <p>
	 * This provides a safe fallback when an opcode has not been registered.
	 */
	@NotNull
	public OpcodeHandler get(@NotNull Opcode pOpcode) {
		OpcodeHandler handler = handlers.get(pOpcode);
		return Objects.requireNonNullElseGet(handler, () -> (pContext, pStep) -> ExecutionResult.skipped("No handler for opcode: " + pOpcode));
	}

	public boolean has(@NotNull Opcode pOpcode) {
		return handlers.containsKey(pOpcode);
	}

	/**
	 * Returns a snapshot view of the current handlers map (immutable).
	 */
	@NotNull
	public Map<Opcode, OpcodeHandler> snapshot() {
		return Map.copyOf(handlers);
	}
}
