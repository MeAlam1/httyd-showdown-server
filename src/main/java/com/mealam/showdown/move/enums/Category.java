/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.move.enums;

public enum Category {

	PHYSICAL,
	RANGED,
	STATUS;

	public static Category fromString(String pCategory) {
		try {
			return Category.valueOf(pCategory.toLowerCase());
		} catch (IllegalArgumentException pException) {
			throw new IllegalArgumentException("Unknown category: " + pCategory);
		}
	}
}
