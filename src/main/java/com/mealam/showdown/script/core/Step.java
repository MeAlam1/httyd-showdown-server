/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.core;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One executable step in a script program.
 *
 * @param opcode    operation identifier
 * @param args      opcode-specific arguments
 * @param condition optional boolean guard expression
 * @param chance    optional chance expression (0..1)
 */
public record Step(
		@NotNull Opcode opcode,
		@NotNull Map<String, Object> args,
		@Nullable String condition,
		@Nullable String chance) {

	public Step {
		args = Map.copyOf(args);
	}

	public Step(@NotNull Opcode pOpcode) {
		this(pOpcode, Map.of(), null, null);
	}

	public Step(@NotNull Opcode pOpcode, @NotNull Map<String, Object> pArguments) {
		this(pOpcode, pArguments, null, null);
	}
}
