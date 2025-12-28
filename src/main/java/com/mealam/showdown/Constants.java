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
	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = Logger.getLogger(NAME);

	public static boolean isLoggingEnabled = true;

	public static class Loader {

		public static final String BASE_PATH = "static/api/";
		public static final String DRAGONS_PREFIX = "dragons";
		public static final String DRAGONS_PATH = BASE_PATH + DRAGONS_PREFIX;
		public static final String MOVES_PREFIX = "moves";
		public static final String MOVES_PATH = BASE_PATH + MOVES_PREFIX;
		public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
		public static final Pattern SUFFIX_STRIPPER = Pattern.compile("\\.json$");
		public static final Pattern PREFIX_STRIPPER = Pattern.compile("^(dob/)((httyd/)|(httyd2/)|(the_hidden_world/)|(rob/)|(rtte/))?");
	}

	/*TODO:
	 * Possibly think about making Hooks in the JSONs, which would allow for easier modding.
	 */
}
