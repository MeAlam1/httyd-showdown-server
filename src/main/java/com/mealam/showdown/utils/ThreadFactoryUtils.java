package com.mealam.showdown.utils;

import java.util.concurrent.ThreadFactory;

public final class ThreadFactoryUtils {
	public static ThreadFactory daemonThreads(String pNamePrefix) {
		return runnable -> {
			Thread t = new Thread(runnable);
			t.setName(pNamePrefix + "-" + t.threadId());
			t.setDaemon(true);
			return t;
		};
	}
}