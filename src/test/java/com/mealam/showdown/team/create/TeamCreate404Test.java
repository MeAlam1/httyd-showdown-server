package com.mealam.showdown.team.create;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamCreate404Test extends TeamBaseTest {

	private static final int STATUS_CODE = 404;
	
	@Test
	void shouldReturnNotFoundForNonExistentTeam() throws Exception {
		HttpResponse<String> response = api.getTeam("non-existent-team-id");
		assertEquals(STATUS_CODE, response.statusCode());
	}

	@Test
	void shouldReturnNotFoundAfterTeamDeleted() throws Exception {
		String payload = loadResource("singleFullDragon");
		HttpResponse<String> createResponse = api.createTeam(payload);
		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());

		api.deleteTeam(teamId);
		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
	}
}