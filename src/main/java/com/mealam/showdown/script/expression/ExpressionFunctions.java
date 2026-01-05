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
import org.jetbrains.annotations.NotNull;

/**
 * Built-in functions available in expressions.
 */
// TODO: Extract to MathUtils + Add a Ton more
public final class ExpressionFunctions {

	private ExpressionFunctions() {}

	@NotNull
	public static Object call(@NotNull String pName, @NotNull List<Object> pArguments) {
		return switch (pName) {
			case "contains" -> contains(pArguments);
			case "clamp" -> clamp(pArguments);
			case "hasItem" -> hasItem(pArguments);
			case "hasAnyTag" -> hasAnyTag(pArguments);
			default -> throw new IllegalArgumentException("Unknown function: " + pName);
		};
	}

	private static boolean contains(@NotNull List<Object> pArguments) {
		requireArgs(pArguments, 2, "contains");
		Object haystack = pArguments.get(0);
		Object needle = pArguments.get(1);

		if (haystack instanceof String s) {
			return s.contains(String.valueOf(needle));
		}
		if (haystack instanceof List<?> list) {
			return list.stream().anyMatch(v -> Coerce.equalsLoose(v, needle));
		}
		return false;
	}

	private static double clamp(@NotNull List<Object> pArguments) {
		requireArgs(pArguments, 3, "clamp");
		double value = Coerce.toNumber(pArguments.get(0));
		double min = Coerce.toNumber(pArguments.get(1));
		double max = Coerce.toNumber(pArguments.get(2));
		return Math.max(min, Math.min(max, value));
	}

	private static boolean hasItem(@NotNull List<Object> pArguments) {
		requireArgs(pArguments, 2, "hasItem");
		Object entity = pArguments.get(0);
		String itemId = String.valueOf(pArguments.get(1));

		if (!(entity instanceof Map<?, ?> map)) return false;

		Object items = map.get("items");
		if (items instanceof List<?> list) {
			return list.stream().anyMatch(it -> Coerce.equalsLoose(it, itemId));
		}

		return Coerce.equalsLoose(map.get("item"), itemId);
	}

	private static boolean hasAnyTag(@NotNull List<Object> pArguments) {
		requireArgs(pArguments, 2, "hasAnyTag");
		Object entity = pArguments.get(0);
		Object tagsArg = pArguments.get(1);

		if (!(tagsArg instanceof List<?> wanted)) {
			throw new IllegalArgumentException("hasAnyTag: second arg must be an array");
		}
		if (!(entity instanceof Map<?, ?> map)) return false;

		Object tags = map.get("tags");
		if (!(tags instanceof List<?> tagList)) return false;

		return wanted.stream().anyMatch(w -> tagList.stream().anyMatch(t -> Coerce.equalsLoose(t, w)));
	}

	private static void requireArgs(@NotNull List<Object> pArguments, int pCount, @NotNull String pFunctionName) {
		if (pArguments.size() != pCount) {
			throw new IllegalArgumentException(pFunctionName + " expects " + pCount + " arguments");
		}
	}
}
