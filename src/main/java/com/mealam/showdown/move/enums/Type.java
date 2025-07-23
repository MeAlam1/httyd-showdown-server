/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.move.enums;

public enum Type {

	GENERAL,
	BOULDER,
	MYSTERY,
	SHARP,
	STOKER,
	STRIKE,
	TIDAL,
	TRACKER;

	public static Type fromString(String pType) {
		try {
			return Type.valueOf(pType.toLowerCase());
		} catch (IllegalArgumentException pException) {
			throw new IllegalArgumentException("Unknown category: " + pType);
		}
	}
}
