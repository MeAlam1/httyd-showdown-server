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
import com.mealam.showdown.script.expression.ScriptEngineExpressionEngine;
import com.mealam.showdown.script.opcode.DefaultOpcodes;
import com.mealam.showdown.script.opcode.OpcodeRegistry;
import com.mealam.showdown.script.runtime.ExecutionContext;
import com.mealam.showdown.script.runtime.SimpleExecutionContext;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Facade providing convenience methods to create and run the script interpreter.
 *
 * <p>This class wires together an {@link Interpreter}, an {@link ExpressionEngine},
 * an {@link OpcodeRegistry} and a default {@link ExecutionContext} to offer simple
 * entry points for running scripts represented either as parsed {@link Step} lists
 * or as JSON source strings.</p>
 *
 * <p>Usage notes:
 * - Use {@link #createDefault()} to obtain an engine with sensible defaults
 * (JavaScript-backed expressions and built-in opcodes).
 * - Use {@link #create(ExpressionEngine, OpcodeRegistry)} or
 * {@link #create(ExpressionEngine, OpcodeRegistry, ExecutionContext)} to supply
 * custom components for advanced scenarios (testing, sandboxing, custom opcodes).</p>
 */
public final class ScriptEngine {

	@NotNull
	private final Interpreter interpreter;

	@NotNull
	private final ExecutionContext defaultContext;

	private ScriptEngine(@NotNull Interpreter pInterpreter, @NotNull ExecutionContext pDefaultContext) {
		this.interpreter = Objects.requireNonNull(pInterpreter, "interpreter");
		this.defaultContext = Objects.requireNonNull(pDefaultContext, "defaultContext");
	}

	/**
	 * Builds a ScriptEngine using the library defaults:
	 * - {@link ScriptEngineExpressionEngine} for expressions
	 * - built-in opcode registry from {@link DefaultOpcodes}
	 * - a {@link SimpleExecutionContext} as the default runtime context
	 */
	@NotNull
	public static ScriptEngine createDefault() {
		ExpressionEngine expr = new ScriptEngineExpressionEngine();
		OpcodeRegistry registry = DefaultOpcodes.createRegistry();
		ExecutionContext context = new SimpleExecutionContext();
		return create(expr, registry, context);
	}

	/**
	 * Create an engine using a custom expression engine and opcode registry.
	 * The default runtime context will be a new {@link SimpleExecutionContext}.
	 */
	@NotNull
	public static ScriptEngine create(@NotNull ExpressionEngine pExpressions, @NotNull OpcodeRegistry pRegistry) {
		return create(pExpressions, pRegistry, new SimpleExecutionContext());
	}

	/**
	 * Create an engine using fully custom components.
	 *
	 * @param pExpressions expression evaluator implementation
	 * @param pRegistry    opcode registry to resolve opcode handlers
	 * @param pContext     default execution context used by convenience run\* methods
	 */
	@NotNull
	public static ScriptEngine create(@NotNull ExpressionEngine pExpressions, @NotNull OpcodeRegistry pRegistry, @NotNull ExecutionContext pContext) {
		return new ScriptEngine(new Interpreter(pExpressions, pRegistry), pContext);
	}

	/**
	 * Execute a pre-parsed list of {@link Step}s against a supplied {@link ExecutionContext}.
	 *
	 * @param pContext runtime context to use for the run
	 * @param pSteps   list of steps to execute
	 * @return list of {@link ExecutionResult} values, one per step
	 */
	public List<ExecutionResult> run(@NotNull ExecutionContext pContext, @NotNull List<Step> pSteps) {
		return interpreter.run(pContext, pSteps);
	}

	/**
	 * Parse the given JSON (top-level or wrapped script object) and execute the
	 * resulting steps against the provided {@link ExecutionContext}.
	 *
	 * @param pContext runtime context to use
	 * @param pJson    JSON script source
	 * @return execution results for each parsed step
	 * @throws IllegalArgumentException if the JSON is invalid or cannot be parsed
	 */
	public List<ExecutionResult> runJson(@NotNull ExecutionContext pContext, @NotNull String pJson) {
		List<Step> steps = JsonScriptLoader.parseOnCast(pJson);
		return interpreter.run(pContext, steps);
	}

	/**
	 * Convenience runner using the engine's default context.
	 *
	 * @param pSteps parsed steps to execute
	 * @return list of execution results
	 */
	public List<ExecutionResult> runDefault(@NotNull List<Step> pSteps) {
		return interpreter.run(defaultContext, pSteps);
	}

	/**
	 * Convenience runner for JSON scripts using the engine's default context.
	 *
	 * @param pJson script JSON to parse and run
	 * @return list of execution results
	 */
	public List<ExecutionResult> runJsonDefault(@NotNull String pJson) {
		List<Step> steps = JsonScriptLoader.parseOnCast(pJson);
		return interpreter.run(defaultContext, steps);
	}

	/**
	 * Returns the underlying opcode registry. Callers may inspect or register
	 * additional opcode handlers for custom behavior.
	 */
	@NotNull
	public OpcodeRegistry registry() {
		return interpreter.registry();
	}
}
