package com.mealam.showdown;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Constants {
	public static final String ID = "httyd-showdown-server";
	public static final String NAME = "HTTYD Showdown";
	public static final Logger LOGGER = Logger.getLogger(NAME);
	public static final Executor BACKGROUND_EXECUTOR = Executors.newSingleThreadExecutor();
	public static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();


	public static boolean isLoggingEnabled = true;

	public static class Loader {
		public static final String DRAGONS_PATH = "static/api/dragons";
		public static final Pattern SUFFIX_STRIPPER = Pattern.compile("\\.json$");
		public static final Pattern PREFIX_STRIPPER = Pattern.compile("^(dob/)((httyd/)|(httyd2/)|(the_hidden_world/)|(rob/)|(rtte/))?");
	}
}
