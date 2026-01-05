/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BattleTestUtils {

	public static String generateRandomUserId() {
		return "user_" + UUID.randomUUID().toString().substring(0, 8);
	}

	public static String extractBattleIdFromBody(String pBody) {
		Pattern p = Pattern.compile("\"battleId\"\\s*:\\s*\"([A-Za-z0-9_-]+)\"");
		Matcher m = p.matcher(pBody);
		if (m.find()) return m.group(1);
		throw new IllegalStateException("Could not extract id from response: " + pBody);
	}

	public static int countOccurrences(String pText, String pTarget) {
		return (pText.length() - pText.replace(pTarget, "").length()) / pTarget.length();
	}
}
