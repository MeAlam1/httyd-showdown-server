package com.mealam.showdown.team.delete;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

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