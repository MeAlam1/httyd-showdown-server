package com.mealam.showdown.script.expression;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Helper functions exposed to the scripting environment.
 * These correspond to the previous built-in expression functions.
 * <p>
 * Implementations are defensive and accept Strings, Collections, Maps and arrays.
 */
public final class ExpressionFunctions {

	public ExpressionFunctions() {
	}

	/**
	 * Returns true if container contains value.
	 * - If container is a String and value is non-null, checks substring.
	 * - If container is a Collection, uses Collection.contains.
	 * - If container is a Map, checks keys for matching value.
	 * - If container is an array, checks any element equals value.
	 */
	public boolean contains(Object container, Object value) {
		if (container == null || value == null) return false;

		if (container instanceof String cs) {
			return cs.contains(value.toString());
		}
		if (container instanceof Collection<?> coll) {
			return coll.contains(value);
		}
		if (container instanceof Map<?, ?> map) {
			return map.containsKey(value);
		}
		if (container.getClass().isArray()) {
			int len = java.lang.reflect.Array.getLength(container);
			for (int i = 0; i < len; i++) {
				Object el = java.lang.reflect.Array.get(container, i);
				if (Objects.equals(el, value)) return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Clamps a value between min and max.
	 */
	public double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Returns true if container contains the item as a value.
	 * - For Collections, delegates to contains.
	 * - For Maps, checks values (not keys).
	 * - For Strings, checks substring equality.
	 * - For arrays, checks any element equals item.
	 */
	public boolean hasItem(Object container, Object item) {
		if (container == null || item == null) return false;

		if (container instanceof Collection<?> coll) {
			return coll.contains(item);
		}
		if (container instanceof Map<?, ?> map) {
			return map.values().contains(item);
		}
		if (container instanceof String s) {
			return s.contains(item.toString());
		}
		if (container.getClass().isArray()) {
			int len = java.lang.reflect.Array.getLength(container);
			for (int i = 0; i < len; i++) {
				Object el = java.lang.reflect.Array.get(container, i);
				if (Objects.equals(el, item)) return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Returns true if the tags collection/array contains any of the wanted tags.
	 * - Accepts tags as Collection, Map (keys), array, or String (comma separated).
	 * - Accepts wanted as Collection, array, or single value.
	 */
	public boolean hasAnyTag(Object tags, Object wanted) {
		if (tags == null || wanted == null) return false;

		// Normalize tags into a Collection<Object>
		Collection<?> tagColl = toCollection(tags);
		if (tagColl == null || tagColl.isEmpty()) return false;

		// Normalize wanted into a Collection<Object>
		Collection<?> wantColl = toCollection(wanted);
		if (wantColl == null || wantColl.isEmpty()) {
			// single wanted value
			return tagColl.stream().anyMatch(t -> Objects.equals(t, wanted));
		}

		for (Object w : wantColl) {
			for (Object t : tagColl) {
				if (Objects.equals(t, w)) return true;
				if (t != null && w != null && t.toString().equalsIgnoreCase(w.toString())) return true;
			}
		}
		return false;
	}

	private Collection<?> toCollection(Object o) {
		if (o instanceof Collection<?> c) return c;
		if (o instanceof Map<?, ?> m) return m.keySet();
		if (o.getClass().isArray()) {
			int len = java.lang.reflect.Array.getLength(o);
			java.util.List<Object> list = new java.util.ArrayList<>(len);
			for (int i = 0; i < len; i++) {
				list.add(java.lang.reflect.Array.get(o, i));
			}
			return list;
		}
		if (o instanceof String s) {
			// split comma/space separated tags
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