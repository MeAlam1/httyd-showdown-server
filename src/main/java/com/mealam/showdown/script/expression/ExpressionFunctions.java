/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Utility functions exposed to scripting expression environments.
 *
 * <p>These helper methods provide consistent, defensive behaviors for common
 * script operations such as containment checks, clamping numeric values, and
 * tag membership testing. They accept a variety of container types (String,
 * Collection, Map, array) and handle nulls gracefully.</p>
 */
public final class ExpressionFunctions {

	public ExpressionFunctions() {}

	/**
	 * Returns true if container contains value.
	 * - If container is a String and value is non-null, checks substring.
	 * - If container is a Collection, uses Collection.contains.
	 * - If container is a Map, checks keys for matching value.
	 * - If container is an array, checks any element equals value.
	 */
	public boolean contains(@NotNull Object pContainer, @NotNull Object pValue) {
		return switch (pContainer) {
			case String cs -> cs.contains(pValue.toString());
			case Collection<?> coll -> coll.contains(pValue);
			case Map<?, ?> map -> map.containsKey(pValue);
			default -> handleArray(pContainer, pValue);
		};
	}

	private boolean handleArray(@NotNull Object pContainer, @NotNull Object pValue) {
		if (pContainer.getClass().isArray()) {
			int len = java.lang.reflect.Array.getLength(pContainer);
			for (int i = 0; i < len; i++) {
				Object el = java.lang.reflect.Array.get(pContainer, i);
				if (Objects.equals(el, pValue)) return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Clamps a value between min and max.
	 */
	public double clamp(double pValue, double pMin, double pMax) {
		return Math.max(pMin, Math.min(pMax, pValue));
	}

	/**
	 * Returns true if container contains the item as a value.
	 * - For Collections, delegates to contains.
	 * - For Maps, checks values (not keys).
	 * - For Strings, checks substring equality.
	 * - For arrays, checks any element equals item.
	 */
	public boolean hasItem(@NotNull Object pContainer, @NotNull Object pItem) {
		return switch (pContainer) {
			case Collection<?> coll -> coll.contains(pItem);
			case Map<?, ?> map -> map.containsValue(pItem);
			case String s -> s.contains(pItem.toString());
			default -> handleArray(pContainer, pItem);
		};
	}

	/**
	 * Returns true if the tags collection/array contains any of the wanted tags.
	 * - Accepts tags as Collection, Map (keys), array, or String (comma separated).
	 * - Accepts wanted as Collection, array, or single value.
	 */
	public boolean hasAnyTag(@NotNull Object pTags, @NotNull Object pWanted) {
		Collection<?> tagColl = toCollection(pTags);
		if (tagColl == null || tagColl.isEmpty()) return false;

		Collection<?> wantColl = toCollection(pWanted);
		if (wantColl == null || wantColl.isEmpty()) {
			return tagColl.stream().anyMatch(t -> Objects.equals(t, pWanted));
		}

		for (Object w : wantColl) {
			for (Object t : tagColl) {
				if (Objects.equals(t, w)) return true;
				if (t != null && w != null && t.toString().equalsIgnoreCase(w.toString())) return true;
			}
		}
		return false;
	}

	@NotNull
	private Collection<?> toCollection(@NotNull Object pObject) {
		if (pObject instanceof Collection<?> c) return c;
		if (pObject instanceof Map<?, ?> m) return m.keySet();
		if (pObject.getClass().isArray()) {
			int len = java.lang.reflect.Array.getLength(pObject);
			java.util.List<Object> list = new java.util.ArrayList<>(len);
			for (int i = 0; i < len; i++) {
				list.add(java.lang.reflect.Array.get(pObject, i));
			}
			return list;
		}
		if (pObject instanceof String s) {
			String[] parts = s.split("\\s*,\\s*|\\s+");
			java.util.List<String> list = new java.util.ArrayList<>();
			for (String p : parts) {
				if (!p.isBlank()) list.add(p);
			}
			return list;
		}
		return null;
	}
}
