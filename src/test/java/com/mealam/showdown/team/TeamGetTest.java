package com.mealam.showdown.team;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class TeamGetTest extends TeamBaseTest {

	@Test
	void getTeamNotFound() throws Exception {
		HttpResponse<String> res = api.getTeam("does-not-exist");
		assertEquals(404, res.statusCode());
	}

	@Test
	void getTeamReturnsCreatedTeam() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"lookup\",\"dragonIds\":[]}");
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("lookup"));
	}
}