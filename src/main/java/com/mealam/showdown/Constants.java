package com.mealam.showdown;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class Constants {

	public static final String ID = "httyd-showdown-server";
	public static final String NAME = "HTTYD Showdown";
	public static final String VERSION = "1.0.0";

	public static final Logger LOGGER = Logger.getLogger(NAME);


	public static volatile boolean isLoggingEnabled = true;

	private Constants() {
	}

	public static final class Loader {

		public static final String BASE_PATH = "static/api";

		public static final String DRAGONS_PREFIX = "dragons";
		public static final String MOVES_PREFIX = "moves";
		public static final String ITEMS_PREFIX = "heldItems";
		public static final String NATURES_PREFIX = "natures";
		public static final String ABILITIES_PREFIX = "abilities";

		public static final String DRAGONS_PATH = String.join("/", BASE_PATH, DRAGONS_PREFIX);
		public static final String MOVES_PATH = String.join("/", BASE_PATH, MOVES_PREFIX);
		public static final String ITEMS_PATH = String.join("/", BASE_PATH, ITEMS_PREFIX);
		public static final String NATURES_PATH = String.join("/", BASE_PATH, NATURES_PREFIX);
		public static final String ABILITIES_PATH = String.join("/", BASE_PATH, ABILITIES_PREFIX);

		public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

		public static final Pattern SUFFIX_STRIPPER = Pattern.compile("\\.json$");
		public static final Pattern PREFIX_STRIPPER = Pattern.compile(
				"^(dob/)((httyd/)|(httyd2/)|(the_hidden_world/)|(rob/)|(rtte/))?"
		);

		private Loader() {
		}
	}
}