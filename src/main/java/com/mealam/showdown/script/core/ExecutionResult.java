/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.core;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the outcome of executing a single {@link Step}.
 *
 * <p>Immutable record with:
 * - {@code executed}: true when the step handler ran, false when the step was skipped
 * - {@code logs}: an immutable list of diagnostic messages describing the execution</p>
 */
public record ExecutionResult(boolean executed, @NotNull List<String> logs) {

	public ExecutionResult {
		logs = List.copyOf(logs);
	}

	@NotNull
	public static ExecutionResult executed(@NotNull String pLog) {
		return new ExecutionResult(true, List.of(pLog));
	}

	@NotNull
	public static ExecutionResult skipped(@NotNull String pLog) {
		return new ExecutionResult(false, List.of(pLog));
	}
}
