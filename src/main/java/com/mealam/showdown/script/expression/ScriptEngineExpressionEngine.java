package com.mealam.showdown.script.expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.script.*;
import java.util.Map;
import java.util.Objects;

/**
 * Expression engine backed by JSR-223 ScriptEngine (JavaScript).
 */
public final class ScriptEngineExpressionEngine implements ExpressionEngine {

	@NotNull
	private final ScriptEngine engine;

	public ScriptEngineExpressionEngine() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine eng = mgr.getEngineByName("JavaScript");
		if (eng == null) {
			eng = mgr.getEngineByName("nashorn");
		}
		if (eng == null) {
			throw new IllegalStateException("No JavaScript ScriptEngine available");
		}
		this.engine = eng;

		engine.put("__f", new ExpressionFunctions());
		try {
			engine.eval(
					"function contains(a,b){ return __f.contains(a,b); }\n" +
							"function clamp(a,b,c){ return __f.clamp(a,b,c); }\n" +
							"function hasItem(a,b){ return __f.hasItem(a,b); }\n" +
							"function hasAnyTag(a,b){ return __f.hasAnyTag(a,b); }\n"
			);
		} catch (ScriptException e) {
			throw new RuntimeException("Failed to initialize expression helpers", e);
		}
	}

	@Override
	public double evaluateNumber(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		Bindings bindings = createBindings(pVariables);
		try {
			Object result = engine.eval(pExpression, bindings);
			return toNumber(result);
		} catch (ScriptException e) {
			return 0.0;
		}
	}

	@Override
	public boolean evaluateBool(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		Bindings bindings = createBindings(pVariables);
		try {
			Object result = engine.eval(pExpression, bindings);
			return toBool(result);
		} catch (ScriptException e) {
			return false;
		}
	}

	@NotNull
	private Bindings createBindings(@NotNull Map<String, Object> pVariables) {
		SimpleBindings b = new SimpleBindings();
		if (!pVariables.isEmpty()) {
			b.putAll(pVariables);
		}
		return b;
	}

	private static double toNumber(@Nullable Object pValue) {
		if (pValue == null) return 0.0;
		if (pValue instanceof Number n) return n.doubleValue();
		if (pValue instanceof Boolean b) return b ? 1.0 : 0.0;
		if (pValue instanceof String s) {
			try {
				return Double.parseDouble(s.trim());
			} catch (NumberFormatException ignored) {
				return 0.0;
			}
		}
		return 0.0;
	}

	private static boolean toBool(@Nullable Object pValue) {
		if (pValue == null) return false;
		if (pValue instanceof Boolean b) return b;
		if (pValue instanceof Number n) return n.doubleValue() != 0.0;
		if (pValue instanceof String s) return !s.isBlank() && !"false".equalsIgnoreCase(s) && !"0".equals(s);
		return true;
	}
}