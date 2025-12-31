package com.mealam.showdown.team.delete;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TeamDelete404Test extends TeamBaseTest {

	private static final int STATUS_CODE = 404;

	@Test
	void shouldReturnNotFoundWhenDeletingMissingTeam() throws Exception {
		HttpResponse<String> res = api.deleteTeam("missing-team");
		assertEquals(STATUS_CODE, res.statusCode());
	}

	@Test
	void shouldReturnNotFoundAfterTeamDeleted() throws Exception {
		String payload = loadResource("singleFullDragon");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		api.deleteTeam(teamId);
		HttpResponse<String> getAfterDelete = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getAfterDelete.statusCode());
	}

	@Test
	void shouldReturnNotFoundWhenDeletingTeamTwice() throws Exception {
		String payload = loadResource("singleFullDragon");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		assertEquals(200, api.deleteTeam(teamId).statusCode());
		assertEquals(STATUS_CODE, api.deleteTeam(teamId).statusCode());
	}
}