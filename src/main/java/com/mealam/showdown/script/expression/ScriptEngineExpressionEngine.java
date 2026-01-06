/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Expression engine backed by JSR-223 ScriptEngine (JavaScript).
 * Uses a per-thread ScriptEngine instance, basic input sanitization to reduce attack surface,
 * and optional debug logging for script errors.
 */
public final class ScriptEngineExpressionEngine implements ExpressionEngine {

	private static final Logger LOGGER = Logger.getLogger(ScriptEngineExpressionEngine.class.getName());

	@NotNull
	private final ThreadLocal<ScriptEngine> engineTl;
	private final boolean debug;

	public ScriptEngineExpressionEngine() {
		this(false);
	}

	/**
	 * @param pDebug when true, script exceptions and rejected expressions are logged at FINE level.
	 */
	public ScriptEngineExpressionEngine(boolean pDebug) {
		this.debug = pDebug;
		this.engineTl = ThreadLocal.withInitial(this::createEngine);
	}

	@NotNull
	private ScriptEngine createEngine() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine eng = mgr.getEngineByName("JavaScript");
		if (eng == null) {
			eng = mgr.getEngineByName("nashorn");
		}
		if (eng == null) {
			throw new IllegalStateException("No JavaScript ScriptEngine available");
		}

		eng.put("__f", new ExpressionFunctions());
		try {
			eng.eval(
					"function contains(a,b){ return __f.contains(a,b); }\n" +
							"function clamp(a,b,c){ return __f.clamp(a,b,c); }\n" +
							"function hasItem(a,b){ return __f.hasItem(a,b); }\n" +
							"function hasAnyTag(a,b){ return __f.hasAnyTag(a,b); }\n");
		} catch (ScriptException e) {
			throw new RuntimeException("Failed to initialize expression helpers", e);
		}
		return eng;
	}

	@Override
	public double evaluateNumber(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		if (isUnsafe(pExpression)) {
			if (debug) LOGGER.log(Level.FINE, "Rejected unsafe numeric expression: {0}", pExpression);
			return 0.0;
		}

		Bindings bindings = createBindings(pVariables);
		try {
			Object result = engineTl.get().eval(pExpression, bindings);
			return toNumber(result);
		} catch (ScriptException e) {
			if (debug)
				LOGGER.log(Level.FINE, "ScriptException evaluateNumber for \"{0}\": {1}", new Object[] { pExpression, e.getMessage() });
			else LOGGER.log(Level.FINE, "Expression evaluation failed");
			return 0.0;
		} catch (Throwable t) {
			LOGGER.log(Level.WARNING, "Unexpected error during expression evaluation", t);
			return 0.0;
		}
	}

	@Override
	public boolean evaluateBool(@NotNull String pExpression, @NotNull Map<String, Object> pVariables) {
		if (isUnsafe(pExpression)) {
			if (debug) LOGGER.log(Level.FINE, "Rejected unsafe boolean expression: {0}", pExpression);
			return false;
		}

		Bindings bindings = createBindings(pVariables);
		try {
			Object result = engineTl.get().eval(pExpression, bindings);
			return toBool(result);
		} catch (ScriptException e) {
			if (debug)
				LOGGER.log(Level.FINE, "ScriptException evaluateBool for \"{0}\": {1}", new Object[] { pExpression, e.getMessage() });
			else LOGGER.log(Level.FINE, "Expression evaluation failed");
			return false;
		} catch (Throwable t) {
			LOGGER.log(Level.WARNING, "Unexpected error during expression evaluation", t);
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

	/**
	 * Basic heuristic filter to block obviously dangerous expressions that try to access Java types or system APIs.
	 * This is not a full sandbox; for untrusted input consider a proper sandboxing mechanism.
	 */
	private boolean isUnsafe(@Nullable String pExpression) {
		if (pExpression == null) return false;
		String lower = pExpression.toLowerCase();
		String[] bad = new String[] {
				"java.", "java::", "packages.", "java.lang", "javax.", "javax.", "javafx.", "class.forname",
				"importpackage", "importpackage(", "import(", "load(", "exit(", "system.", "runtime.", "new java",
				"getclass(", "constructor", "javax.script", "engine.eval("
		};
		for (String b : bad) {
			if (lower.contains(b)) return true;
		}
		return false;
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
