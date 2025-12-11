package com.mealam.showdown.utils.logging;

import com.mealam.showdown.Constants;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class BaseLogger {

	private static final ExecutorService LOG_EXECUTOR = Executors.newSingleThreadExecutor(r -> new Thread(r, "logger"));

	private BaseLogger() {
	}

	static {
		LoggerConfig.configure(Constants.LOGGER, new DefaultLogColorProvider());
	}

	public static @NotNull String logAndReturn(@NotNull Level pLogLevel, @NotNull String pMessage) {
		log(pLogLevel, pMessage);
		return pMessage;
	}

	public static @NotNull String logAndReturn(@NotNull Level pLogLevel, @NotNull String pMessage, @NotNull Throwable... pThrowable) {
		log(pLogLevel, pMessage, pThrowable);
		return pMessage;
	}

	public static void log(@NotNull Level pLogLevel, @NotNull Supplier<String> pMessageSupplier, @NotNull Throwable... pThrowable) {
		final Throwable[] throwables = Arrays.copyOf(pThrowable, pThrowable.length);
		runAsync(() -> {
			if (shouldLog(pLogLevel)) {
				logInternal(pLogLevel, pMessageSupplier.get(), throwables);
			}
		});
	}

	public static void log(@NotNull Level pLogLevel, @NotNull Supplier<String> pMessageSupplier) {
		runAsync(() -> {
			if (shouldLog(pLogLevel)) {
				logInternal(pLogLevel, pMessageSupplier.get());
			}
		});
	}

	public static void log(@NotNull Level pLogLevel, @NotNull String pMessage, @NotNull Throwable... pThrowable) {
		final Throwable[] throwables = Arrays.copyOf(pThrowable, pThrowable.length);
		runAsync(() -> {
			if (shouldLog(pLogLevel)) {
				logInternal(pLogLevel, pMessage, throwables);
			}
		});
	}

	public static void log(@NotNull Level pLogLevel, @NotNull String pMessage) {
		runAsync(() -> {
			if (shouldLog(pLogLevel)) {
				logInternal(pLogLevel, pMessage);
			}
		});
	}

	public static void bypassAndLog(@NotNull String pMessage) {
		runAsync(() -> Constants.LOGGER.log(BaseLogLevel.OVERRIDE, pMessage));
	}

	// --- Internal helpers ---

	private static @NotNull Boolean shouldLog(@NotNull Level pLogLevel) {
		return Constants.isLoggingEnabled || pLogLevel == BaseLogLevel.ERROR ||
				pLogLevel == BaseLogLevel.WARNING ||
				pLogLevel == BaseLogLevel.OVERRIDE;
	}

	private static void runAsync(@NotNull Runnable pTask) {
		LOG_EXECUTOR.execute(() -> {
			try {
				pTask.run();
			} catch (Throwable pThrowable) {
				try {
					Constants.LOGGER.log(BaseLogLevel.ERROR, "Logger task failed", pThrowable);
				} catch (Throwable pIgnored) {
					throw new RuntimeException("Logger failed and could not log the failure.", pIgnored);
				}
			}
		});
	}

	@ApiStatus.Internal
	private static void logInternal(@NotNull Level pLogLevel, @NotNull String pMessage) {
		Constants.LOGGER.log(pLogLevel, pMessage);
	}

	@ApiStatus.Internal
	private static void logInternal(Level pLogLevel, String pMessage, Throwable... pThrowable) {
		Constants.LOGGER.log(pLogLevel, pMessage, pThrowable);
	}
}