/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Abstraction for evaluating string-based expressions against a variable context.
 *
 * <p>Implementations should convert the provided expression into either a numeric
 * value via {@link #evaluateNumber(String, Map)} or a boolean via
 * {@link #evaluateBool(String, Map)}. The evaluation is performed using the supplied
 * variables map as the execution context.</p>
 */
public interface ExpressionEngine {

	double evaluateNumber(@NotNull String pExpression, @NotNull Map<String, Object> pVariables);

	boolean evaluateBool(@NotNull String pExpression, @NotNull Map<String, Object> pVariables);
}
