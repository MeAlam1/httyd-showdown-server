/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TeamTestUtils {

	private TeamTestUtils() {}

	// Accepts JSON like: {"team":{"teamId":{"value":"abc"}}...} or {"team":{"teamId":"abc"...}}
	private static final Pattern TEAM_ID_VALUE = Pattern.compile("\"teamId\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*\"([^\"]+)\"\\s*\\}");
	private static final Pattern TEAM_ID_STRING = Pattern.compile("\"teamId\"\\s*:\\s*\"([^\"]+)\"");

	public static String extractTeamIdFromBody(String pBody) {
		if (pBody == null) return "";

		Matcher m1 = TEAM_ID_VALUE.matcher(pBody);
		if (m1.find()) return m1.group(1);

		Matcher m2 = TEAM_ID_STRING.matcher(pBody);
		if (m2.find()) return m2.group(1);

		return "";
	}
}
