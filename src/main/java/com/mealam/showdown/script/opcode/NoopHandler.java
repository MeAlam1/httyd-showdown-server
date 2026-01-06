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
import com.mealam.showdown.script.runtime.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * No-operation handler for the NOOP opcode.
 *
 * <p>Useful as a placeholder, comment, or marker within scripts. Always returns
 * an executed result with a simple diagnostic message.</p>
 */
public final class NoopHandler implements OpcodeHandler {

	@NotNull
	public static final NoopHandler INSTANCE = new NoopHandler();

	private NoopHandler() {}

	@Override
	public @NotNull ExecutionResult execute(@NotNull ExecutionContext pContext, @NotNull Step pStep) {
		return ExecutionResult.executed("NOOP");
	}
}
