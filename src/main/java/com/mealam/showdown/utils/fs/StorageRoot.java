package com.mealam.showdown.utils.fs;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class StorageRoot {

	private StorageRoot() {
	}

	/**
	 * Resolve app data root:
	 * 1) Override: system property `showdown.data.dir` or env `SHOWDOWN_DATA_DIR`
	 * 2) Windows: %LOCALAPPDATA%\Showdown
	 * 3) Others: ~/.showdown
	 */
	public static Path resolveAppDataRoot() {
		String override = System.getProperty("showdown.data.dir");
		if (override == null || override.isBlank()) {
			override = System.getenv("SHOWDOWN_DATA_DIR");
		}
		if (override != null && !override.isBlank()) {
			return Paths.get(override);
		}

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			String localAppData = System.getenv("LOCALAPPDATA");
			if (localAppData != null && !localAppData.isBlank()) {
				return Paths.get(localAppData, "Showdown");
			}
			String userHome = System.getProperty("user.home", ".");
			return Paths.get(userHome, "AppData", "Local", "Showdown");
		} else {
			String userHome = System.getProperty("user.home", ".");
			return Paths.get(userHome, ".showdown");
		}
	}
}