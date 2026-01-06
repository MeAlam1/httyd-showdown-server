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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Main entry point for script execution.
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

	@NotNull
	public static ScriptEngine createDefault() {
		ExpressionEngine expr = new ScriptEngineExpressionEngine();
		OpcodeRegistry registry = DefaultOpcodes.createRegistry();
		ExecutionContext context = new SimpleExecutionContext();
		return create(expr, registry, context);
	}

	@NotNull
	public static ScriptEngine create(@NotNull ExpressionEngine pExpressions, @NotNull OpcodeRegistry pRegistry) {
		return create(pExpressions, pRegistry, new SimpleExecutionContext());
	}

	@NotNull
	public static ScriptEngine create(@NotNull ExpressionEngine pExpressions, @NotNull OpcodeRegistry pRegistry, @NotNull ExecutionContext pContext) {
		return new ScriptEngine(new Interpreter(pExpressions, pRegistry), pContext);
	}

	public List<ExecutionResult> run(@NotNull ExecutionContext pContext, @NotNull List<Step> pSteps) {
		return interpreter.run(pContext, pSteps);
	}

	public List<ExecutionResult> runJson(@NotNull ExecutionContext pContext, @NotNull String pJson) {
		List<Step> steps = JsonScriptLoader.parseOnCast(pJson);
		return interpreter.run(pContext, steps);
	}

	/**
	 * Runs using the engine's default context.
	 */
	public List<ExecutionResult> runDefault(@NotNull List<Step> pSteps) {
		return interpreter.run(defaultContext, pSteps);
	}

	/**
	 * Runs JSON using the engine's default context.
	 */
	public List<ExecutionResult> runJsonDefault(@NotNull String pJson) {
		List<Step> steps = JsonScriptLoader.parseOnCast(pJson);
		return interpreter.run(defaultContext, steps);
	}

	@NotNull
	public OpcodeRegistry registry() {
		return interpreter.registry();
	}
}