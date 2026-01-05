/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.delete;

import static org.junit.jupiter.api.Assertions.*;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class TeamDelete200Test extends TeamBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldDeleteTeamSuccessfully() throws Exception {
		String payload = loadResource("singleFullDragon");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		HttpResponse<String> deleteResponse = api.deleteTeam(teamId);
		assertEquals(STATUS_CODE, deleteResponse.statusCode());
		assertTrue(deleteResponse.body().contains("deleted"));
	}
}
