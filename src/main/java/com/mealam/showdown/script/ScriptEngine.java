/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script;

import com.mealam.showdown.script.core.ExecutionResult;
import com.mealam.showdown.script.core.Interpreter;
import com.mealam.showdown.script.core.JsonScriptLoader;
import com.mealam.showdown.script.core.Step;
import com.mealam.showdown.script.expression.ExpressionEngine;
import com.mealam.showdown.script.expression.SimpleExpressionEngine;
import com.mealam.showdown.script.opcode.DefaultOpcodes;
import com.mealam.showdown.script.opcode.OpcodeRegistry;
import com.mealam.showdown.script.runtime.ExecutionContext;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Main entry point for script execution.
 * <p>
 * Example usage:
 * 
 * <pre>
 * 
 * var engine = ScriptEngine.createDefault();
 * var results = engine.runJson(context, abilityJson);
 * </pre>
 */
public final class ScriptEngine {

	@NotNull
	private final Interpreter interpreter;

	private ScriptEngine(@NotNull Interpreter pInterpreter) {
		this.interpreter = Objects.requireNonNull(pInterpreter, "interpreter");
	}

	@NotNull
	public static ScriptEngine createDefault() {
		return create(SimpleExpressionEngine.INSTANCE, DefaultOpcodes.createRegistry());
	}

	public static ScriptEngine create(@NotNull ExpressionEngine pExpressions, @NotNull OpcodeRegistry pRegistry) {
		return new ScriptEngine(new Interpreter(pExpressions, pRegistry));
	}

	public List<ExecutionResult> run(@NotNull ExecutionContext pContext, @NotNull List<Step> pSteps) {
		return interpreter.run(pContext, pSteps);
	}

	public List<ExecutionResult> runJson(@NotNull ExecutionContext pContext, @NotNull String pJson) {
		List<Step> steps = JsonScriptLoader.parseOnCast(pJson);
		return interpreter.run(pContext, steps);
	}

	@NotNull
	public OpcodeRegistry registry() {
		return interpreter.registry();
	}
}
