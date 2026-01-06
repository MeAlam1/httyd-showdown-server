/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Lightweight parser that converts JSON script representations into a list of {@link Step}s.
 *
 * <p>Accepted JSON shapes:
 * - A top-level object with a nested script block: `{ "script": { "onCast": [ ... ] } }`
 * - A top-level object that directly contains an `onCast` array: `{ "onCast": [ ... ] }`</p>
 *
 * <p>Parsing rules:
 * - Missing or invalid JSON results in an {@link IllegalArgumentException}.
 * - Steps must contain a non-blank {@code opcode} string; unknown opcodes will cause an exception.
 * - Arguments are recursively parsed into immutable Java values:
 * strings, numbers, booleans, lists and maps. Null/missing fields yield empty maps or `null` values
 * where appropriate.</p>
 */
public final class JsonScriptLoader {

	@NotNull
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JsonScriptLoader() {}

	/**
	 * Parses the top-level JSON and returns the list of steps under the first found \"onCast\" array.
	 *
	 * @throws IllegalArgumentException when JSON is invalid or parsing fails
	 */
	@NotNull
	public static List<Step> parseOnCast(@NotNull String pJson) {
		try {
			JsonNode root = MAPPER.readTree(pJson);
			JsonNode onCast = root.path("script").path("onCast");
			if (onCast.isMissingNode() || !onCast.isArray()) {
				// fallback to top-level "onCast"
				onCast = root.path("onCast");
			}
			return parseSteps(onCast);
		} catch (IOException pIoException) {
			throw new IllegalArgumentException("Failed to parse script JSON: invalid JSON", pIoException);
		} catch (Exception pException) {
			throw new IllegalArgumentException("Failed to parse script JSON", pException);
		}
	}

	/**
	 * Parses a JsonNode that is expected to be an array of steps.
	 * Returns an empty list if the node is not an array.
	 */
	@NotNull
	public static List<Step> parseSteps(@NotNull JsonNode pArray) {
		if (!pArray.isArray()) {
			return List.of();
		}

		List<Step> steps = new ArrayList<>();
		for (JsonNode node : pArray) {
			steps.add(parseStep(node));
		}
		return steps;
	}

	@NotNull
	private static Step parseStep(@NotNull JsonNode pNode) {
		String opcodeRaw = pNode.path("opcode").asText(null);
		if (opcodeRaw == null || opcodeRaw.isBlank()) {
			throw new IllegalArgumentException("Step missing opcode");
		}

		Opcode opcode;
		try {
			opcode = Opcode.valueOf(opcodeRaw);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unknown opcode: " + opcodeRaw, e);
		}

		String condition = textOrNull(pNode, "condition");
		String chance = textOrNull(pNode, "chance");
		Map<String, Object> args = parseObject(pNode.path("args"));

		return new Step(opcode, args, condition, chance);
	}

	private static @Nullable String textOrNull(@NotNull JsonNode pNode, @NotNull String pField) {
		return pNode.hasNonNull(pField) ? pNode.get(pField).asText() : null;
	}

	/**
	 * Parses a JSON object node into an immutable Map\<String,Object\>.
	 * Non-object nodes (null/missing) return an empty map.
	 */
	@NotNull
	private static Map<String, Object> parseObject(@NotNull JsonNode pNode) {
		if (pNode.isMissingNode() || pNode.isNull() || !pNode.isObject()) {
			return Map.of();
		}

		Map<String, Object> map = new LinkedHashMap<>();
		Iterator<Map.Entry<String, JsonNode>> fields = pNode.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> entry = fields.next();
			map.put(entry.getKey(), parseValue(entry.getValue()));
		}
		return Map.copyOf(map);
	}

	/**
	 * Parses a JsonNode into a corresponding Java value:
	 * - text -> String
	 * - number -> Number
	 * - boolean -> Boolean
	 * - array -> List\<Object\> (elements parsed recursively)
	 * - object -> Map\<String,Object\> (parsed recursively)
	 * - null/missing -> null
	 */
	private static @Nullable Object parseValue(@NotNull JsonNode pNode) {
		if (pNode.isMissingNode() || pNode.isNull()) return null;
		if (pNode.isTextual()) return pNode.asText();
		if (pNode.isNumber()) return pNode.numberValue();
		if (pNode.isBoolean()) return pNode.asBoolean();
		if (pNode.isArray()) {
			List<Object> list = new ArrayList<>();
			for (JsonNode item : pNode) {
				list.add(parseValue(item));
			}
			return List.copyOf(list);
		}
		if (pNode.isObject()) return parseObject(pNode);
		return pNode.asText();
	}
}
