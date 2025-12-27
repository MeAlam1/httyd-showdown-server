package com.mealam.showdown.team;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class TeamCreateTest extends TeamBaseTest {

	@Test
	void createTeam() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"alpha-team\",\"dragonIds\":[\"d1\",\"d2\"]}");
		assertEquals(201, createResponse.statusCode());

		String body = createResponse.body();
		assertNotNull(body);
		assertTrue(body.contains("team"));
		assertTrue(body.contains("teamId"));

		String teamId = TeamTestUtils.extractTeamIdFromBody(body);
		assertNotNull(teamId);
		assertFalse(teamId.isBlank());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("alpha-team"));
	}

	@Test
	void createTeamRejectsMissingName() throws Exception {
		HttpResponse<String> res = api.createTeam("{\"dragonIds\":[\"d1\"]}");
		assertEquals(400, res.statusCode());
	}

	@Test
	void createTeamRejectsBlankName() throws Exception {
		HttpResponse<String> res = api.createTeam("{\"name\":\"   \",\"dragonIds\":[]}");
		assertEquals(400, res.statusCode());
	}

	@Test
	void createTeamAllowsMissingDragonIds() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"no-dragons\"}");
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("no-dragons"));
	}
}