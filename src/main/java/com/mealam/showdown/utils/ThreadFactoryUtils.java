/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
