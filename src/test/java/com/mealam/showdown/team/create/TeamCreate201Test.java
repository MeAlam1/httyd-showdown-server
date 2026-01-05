/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.create;

import static org.junit.jupiter.api.Assertions.*;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class TeamCreate201Test extends TeamBaseTest {

	private static final int STATUS_CODE = 201;

	@Test
	void shouldCreateTeamSuccessfully() throws Exception {
		String payload = loadResource("singleFullDragon");
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());

		String body = response.body();
		assertNotNull(body);
		assertTrue(body.contains("team"));
		assertTrue(body.contains("teamId"));

		String teamId = TeamTestUtils.extractTeamIdFromBody(body);
		assertNotNull(teamId);
		assertFalse(teamId.isBlank());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("alpha-team"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void shouldAllowMissingDragonIds() throws Exception {
		String payload = loadResource("onlyTeamName");
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(response.body());
		assertFalse(teamId.isBlank());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("no-dragons"));
	}

	@Test
	void shouldTrimDragonIds() throws Exception {
		String payload = loadResource("spacesInDragonId");
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(response.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("trim-team"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void shouldIgnoreBlankAndNullLikeDragonIds() throws Exception {
		String payload = loadResource("blankDragonIds");
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(response.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("ignore-blanks"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void shouldDeduplicateDragonIds() throws Exception {
		String payload = loadResource("sameDragonIds");
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(response.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("dupe-team"));

		String responseBody = getResponse.body();
		int first = responseBody.indexOf("flightmare");
		int last = responseBody.lastIndexOf("flightmare");
		assertTrue(first >= 0);
		assertEquals(first, last);
	}
}
