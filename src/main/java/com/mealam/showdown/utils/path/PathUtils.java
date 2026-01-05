/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.path;

import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class PathUtils {

	public static boolean isValidSegment(String pInput) {
		if (pInput == null || pInput.isBlank()) return false;
		return !pInput.contains("..");
	}

	public static String normalize(String pInput) {
		try {
			return Paths.get(pInput).normalize().toString();
		} catch (InvalidPathException pInvalidPathException) {
			return BaseLogger.logAndReturn(BaseLogLevel.ERROR, "Failed to normalize path: " + pInput, pInvalidPathException);
		}
	}
}
