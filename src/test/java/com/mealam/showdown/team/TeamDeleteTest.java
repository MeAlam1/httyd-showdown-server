package com.mealam.showdown.team;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class TeamDeleteTest extends TeamBaseTest {

	@Override
	protected String loadResource(String pResourcePath) throws IOException {
		return super.loadResource("delete/" + pResourcePath);
	}

	@Test
	void deleteTeam() throws Exception {
		String payload = loadResource("deleteTeam.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		HttpResponse<String> deleteResponse = api.deleteTeam(teamId);
		assertEquals(200, deleteResponse.statusCode());
		assertTrue(deleteResponse.body().contains("deleted"));

		HttpResponse<String> getAfterDelete = api.getTeam(teamId);
		assertEquals(404, getAfterDelete.statusCode());
	}

	@Test
	void deleteTeamNotFound() throws Exception {
		HttpResponse<String> res = api.deleteTeam("missing-team");
		assertEquals(404, res.statusCode());
	}

	@Test
	void deleteTeamTwiceSecondIsNotFound() throws Exception {
		String payload = loadResource("deleteTeamTwiceSecondIsNotFound.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		assertEquals(200, api.deleteTeam(teamId).statusCode());
		assertEquals(404, api.deleteTeam(teamId).statusCode());
	}
}