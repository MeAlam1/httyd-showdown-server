/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.opcode;

import com.mealam.showdown.script.core.Opcode;
import org.jetbrains.annotations.NotNull;

/**
 * Registers all built-in opcode handlers and provides a convenience factory
 * for a registry populated with the default handlers.
 *
 * <p>Current built-in handlers include:
 * - {@link NoopHandler} for {@link Opcode#NOOP}
 * - {@link EmitHandler} for {@link Opcode#EMIT}</p>
 */
public final class DefaultOpcodes {

	private DefaultOpcodes() {}

	@NotNull
	public static OpcodeRegistry createRegistry() {
		return registerAll(new OpcodeRegistry());
	}

	@NotNull
	public static OpcodeRegistry registerAll(@NotNull OpcodeRegistry pRegistry) {
		pRegistry.register(Opcode.NOOP, NoopHandler.INSTANCE);
		pRegistry.register(Opcode.EMIT, EmitHandler.INSTANCE);
		return pRegistry;
	}
}
