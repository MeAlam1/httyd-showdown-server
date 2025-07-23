/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.move.enums;

public enum Status {

	FEAR,
	BURN,
	POISON,
	SLEEP,
	NOSLEEP,
	CONFUSION,
	COPY,
	STUNNED,
	HEAL,
	SHOCKED,
	HARM,
	PROTECT,
	MULTIPLY,
	PARALYSIS;

	public static Status fromString(String pStatus) {
		try {
			return Status.valueOf(pStatus.toLowerCase());
		} catch (IllegalArgumentException pException) {
			throw new IllegalArgumentException("Unknown category: " + pStatus);
		}
	}
}
