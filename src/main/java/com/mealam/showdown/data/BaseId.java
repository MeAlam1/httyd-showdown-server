package com.mealam.showdown.data;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public abstract class BaseId {
	private final String value;

	protected BaseId(String pValue) {
		if (pValue == null || pValue.isBlank()) {
			throw new IllegalArgumentException("id cannot be null or blank");
		}
		this.value = pValue;
	}

	@JsonValue
	public String asString() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object pObject) {
		if (this == pObject) return true;
		if (!(pObject instanceof BaseId baseId)) return false;
		return value.equals(baseId.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}