package com.mealam.showdown.utils.logging;

import com.mealam.showdown.Constants;
import java.util.function.Supplier;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class Logger {

	private Logger() {}

	static {
		LoggerExtensionsKt.configure(Constants.LOGGER, new DefaultLogColorProvider());
	}

	public static void log(Level pLogLevel, Supplier<String> pMessageSupplier, Throwable... pThrowable) {
		log(pLogLevel, pMessageSupplier.get(), pThrowable);
	}

	public static void log(Level pLogLevel, Supplier<String> pMessageSupplier) {
		log(pLogLevel, pMessageSupplier.get());
	}

	public static void log(Level pLogLevel, String pMessage, Throwable... pThrowable) {
		if (shouldLog(pLogLevel)) {
			Constants.LOGGER.log(pLogLevel, pMessage, pThrowable);
		}
	}

	public static void log(Level pLogLevel, String pMessage) {
		if (shouldLog(pLogLevel)) {
			Constants.LOGGER.log(pLogLevel, pMessage);
		}
	}

	private static boolean shouldLog(Level pLogLevel) {
		return pLogLevel == LogLevel.ERROR ||
				pLogLevel == LogLevel.WARNING ||
				Constants.isLoggingEnabled;
	}
}
