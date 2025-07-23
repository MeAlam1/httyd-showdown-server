/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.logging;

import java.util.logging.Level;

@SuppressWarnings("unused")
public class DefaultLogColorProvider implements ILogColorProvider {

	@Override
	public int getColor(Level pLevel) {
		if (pLevel == LogLevel.ERROR) {
			return 0xFF0000; // Red
		} else if (pLevel == LogLevel.WARNING) {
			return 0xFFA500; // Orange
		} else if (pLevel == LogLevel.INFO) {
			return 0x5DADE2; // Light Blue
		} else if (pLevel == LogLevel.SUCCESS) {
			return 0x00FF00; // Green
		} else {
			return 0xFFFFFF; // Default to white
		}
	}
}
