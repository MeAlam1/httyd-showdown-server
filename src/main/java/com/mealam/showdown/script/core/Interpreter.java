/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.core;

import com.mealam.showdown.script.expression.ExpressionEngine;
import com.mealam.showdown.script.opcode.OpcodeRegistry;
import com.mealam.showdown.script.runtime.ExecutionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Orchestrates execution of a sequence of script {@link Step}s.
 *
 * <p>The interpreter evaluates optional conditional guards and chance expressions
 * for each step using the provided {@link ExpressionEngine} and performs RNG checks
 * using the {@link ExecutionContext}. Opcode execution is delegated to handlers
 * resolved from the supplied {@link OpcodeRegistry}.</p>
 *
 * <p>Key behaviors:
 * - If a step's {@code condition} is present and evaluates to false, the step is skipped.
 * - If a step's {@code chance} is present, it is evaluated to a number in [0,1] and an RNG
 * roll determines whether the step runs.
 * - Opcode handler execution provides an {@link ExecutionResult} describing whether a step
 * executed and any diagnostic logs.</p>
 */
public final class Interpreter {

	@NotNull
	private final ExpressionEngine expressions;
	@NotNull
	private final OpcodeRegistry registry;

	public Interpreter(@NotNull ExpressionEngine pExpressions, @NotNull OpcodeRegistry pRegistry) {
		this.expressions = pExpressions;
		this.registry = pRegistry;
	}

	/**
	 * Returns the opcode registry used by this interpreter.
	 * The registry implementation is thread-safe; callers should avoid modifying it
	 * concurrently unless intentional.
	 */
	@NotNull
	public OpcodeRegistry registry() {
		return registry;
	}

	@NotNull
	public List<ExecutionResult> run(@NotNull ExecutionContext pContext, @NotNull List<Step> pSteps) {
		List<ExecutionResult> results = new ArrayList<>(pSteps.size());
		for (Step step : pSteps) {
			results.add(runStep(pContext, step));
		}
		return results;
	}

	@NotNull
	public ExecutionResult runStep(@NotNull ExecutionContext pContext, @NotNull Step pStep) {
		Map<String, Object> variables = pContext.variables();

		if (!passesCondition(pStep, variables)) {
			return ExecutionResult.skipped("Skipped by condition");
		}

		if (!passesChance(pContext, pStep, variables)) {
			return ExecutionResult.skipped("Failed chance roll");
		}

		return registry.get(pStep.opcode()).execute(pContext, pStep);
	}

	private boolean passesCondition(@NotNull Step pStep, @NotNull Map<String, Object> pVariables) {
		String condition = pStep.condition();
		return condition == null || expressions.evaluateBool(condition, pVariables);
	}

	/**
	 * Evaluates a step's chance expression (if present) and compares against
	 * the runtime RNG. Uses strict \(less-than\) semantics so 0.0 never executes
	 * and 1.0 always executes.
	 */
	private boolean passesChance(@NotNull ExecutionContext pContext, @NotNull Step pStep, @NotNull Map<String, Object> pVariables) {
		String chanceExpr = pStep.chance();
		if (chanceExpr == null) {
			return true;
		}
		double chance = clamp(expressions.evaluateNumber(chanceExpr, pVariables), 0.0, 1.0);
		// Use < for conventional semantics: chance == 0.0 never executes, chance == 1.0 always executes
		return pContext.rng().nextDouble() < chance;
	}

	// TODO: Extract to MathUtils
	private static double clamp(double pValue, double pMin, double pMax) {
		return Math.max(pMin, Math.min(pMax, pValue));
	}
}
