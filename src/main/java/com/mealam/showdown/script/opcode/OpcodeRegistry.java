/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.opcode;

import com.mealam.showdown.script.core.Opcode;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Registry mapping opcodes to their handlers.
 */
public final class OpcodeRegistry {

	@NotNull
	private final Map<Opcode, OpcodeHandler> handlers = new EnumMap<>(Opcode.class);

	@NotNull
	public OpcodeRegistry register(@NotNull Opcode pOpcode, @NotNull OpcodeHandler pHandler) {
		handlers.put(pOpcode, pHandler);
		return this;
	}

	@NotNull
	public OpcodeHandler get(@NotNull Opcode pOpcode) {
		OpcodeHandler handler = handlers.get(pOpcode);
		if (handler == null) {
			throw new IllegalStateException("No handler for opcode: " + pOpcode);
		}
		return handler;
	}

	public boolean has(@NotNull Opcode pOpcode) {
		return handlers.containsKey(pOpcode);
	}
}
