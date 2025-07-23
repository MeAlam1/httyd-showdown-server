/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.data;

public abstract class BaseId {

	protected final String value;

	protected BaseId(String pValue) {
		this.value = pValue;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
