/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Type coercion utilities for expression evaluation.
 */
public final class Coerce {

	private Coerce() {}

	public static boolean toBool(@NotNull Object pValue) {
		return switch (pValue) {
			case Boolean bool -> bool;
			case Number number -> number.doubleValue() != 0.0;
			case String string -> !string.isBlank() && !"false".equalsIgnoreCase(string) && !"0".equals(string);
			case List<?> list -> !list.isEmpty();
			case Map<?, ?> map -> !map.isEmpty();
			default -> true;
		};
	}

	public static double toNumber(@NotNull Object pValue) {
		switch (pValue) {
			case Number number -> {
				return number.doubleValue();
			}
			case Boolean bool -> {
				return bool ? 1.0 : 0.0;
			}
			case String string -> {
				try {
					return Double.parseDouble(string.trim());
				} catch (NumberFormatException pException) {
					return 0.0;
				}
			}
			default -> {}
		}
		return 0.0;
	}

	public static boolean equalsLoose(@NotNull Object pA, @NotNull Object pB) {
		if (pA == pB) return true;
		if (pA instanceof Number || pB instanceof Number) {
			return Double.compare(toNumber(pA), toNumber(pB)) == 0;
		}
		return Objects.equals(pA, pB);
	}

	@Nullable
	public static Object getProperty(@Nullable Object pBase, @NotNull String pKey) {
		if (pBase instanceof Map<?, ?> m) {
			return m.get(pKey);
		}
		return null;
	}
}
