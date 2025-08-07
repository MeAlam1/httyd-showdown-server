/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Constants {

	public static final String ID = "httyd-showdown-server";
	public static final String NAME = "HTTYD Showdown";
	public static final Logger LOGGER = Logger.getLogger(NAME);

	public static boolean isLoggingEnabled = true;

	public static class Loader {

		private static final String BASE_PATH = "static/api/";
		public static final String DRAGONS_PATH = BASE_PATH + "dragons";
		public static final String MOVES_PATH = BASE_PATH + "moves";
		public static final Pattern SUFFIX_STRIPPER = Pattern.compile("\\.json$");
		public static final Pattern PREFIX_STRIPPER = Pattern.compile("^(dob/)((httyd/)|(httyd2/)|(the_hidden_world/)|(rob/)|(rtte/))?");
	}
}
