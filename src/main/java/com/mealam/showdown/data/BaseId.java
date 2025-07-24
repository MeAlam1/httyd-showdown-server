/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.data;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class BaseId {

	protected final String value;

	protected BaseId(String pValue) {
		this.value = pValue;
	}

	@JsonValue
	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object pObject) {
		if (this == pObject) return true;
		if (!(pObject instanceof BaseId that)) return false;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
