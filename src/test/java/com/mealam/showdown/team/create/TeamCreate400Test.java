/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.create;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mealam.showdown.team.api.TeamBaseTest;
import java.net.http.HttpResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeamCreate400Test extends TeamBaseTest {

	private static final int STATUS_CODE = 400;

	@ParameterizedTest
	@ValueSource(strings = { "noTeamName", "blankTeamName", "unknownDragonId", "tooManyDragons" })
	void shouldRejectInvalidPayloads(String pResourceName) throws Exception {
		String payload = loadResource(pResourceName);
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());
	}
}
