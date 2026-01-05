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
 * Expression engine supporting:
 * <ul>
 * <li>Boolean ops: !, &&, ||</li>
 * <li>Comparisons: ==, !=, <, <=, >, >=</li>
 * <li>Arithmetic: +, -, *, /</li>
 * <li>Parentheses and arrays</li>
 * <li>Dotted paths: caster.stats.power</li>
 * <li>Functions: contains, clamp, hasItem, hasAnyTag</li>
 * </ul>
 */
public final class SimpleExpressionEngine implements ExpressionEngine {

	@NotNull
	public static final SimpleExpressionEngine INSTANCE = new SimpleExpressionEngine();

	@Override
	public double evaluateNumber(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		return Coerce.toNumber(evaluate(pExpression, pVariables));
	}

	@Override
	public boolean evaluateBool(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		return Coerce.toBool(evaluate(pExpression, pVariables));
	}

	private static Object evaluate(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		return new Parser(new Lexer(pExpression), pVariables).parse();
	}
}
