package com.mealam.showdown.team;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class TeamCreateTest extends TeamBaseTest {

	@Test
	void createTeam() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"alpha-team\",\"dragonIds\":[\"flightmare\"]}");
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
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void createTeamRejectsMissingName() throws Exception {
		HttpResponse<String> res = api.createTeam("{\"dragonIds\":[\"flightmare\"]}");
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

	@Test
	void createTeamRejectsUnknownDragonId() throws Exception {
		HttpResponse<String> res = api.createTeam("{\"name\":\"bad-team\",\"dragonIds\":[\"does-not-exist\"]}");
		assertEquals(400, res.statusCode());
	}

	@Test
	void createTeamTrimsDragonIds() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"trim-team\",\"dragonIds\":[\"  flightmare  \"]}");
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("trim-team"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void createTeamIgnoresBlankAndNullLikeDragonIds() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"ignore-blanks\",\"dragonIds\":[\" \",\"flightmare\",\"\"]}");
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("ignore-blanks"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void createTeamDeduplicatesDragonIds() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"dupe-team\",\"dragonIds\":[\"flightmare\",\"flightmare\",\"  flightmare \"]}");
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("dupe-team"));

		String responseBody = getResponse.body();
		int first = responseBody.indexOf("flightmare");
		int last = responseBody.lastIndexOf("flightmare");
		assertTrue(first >= 0);
		assertEquals(first, last);
	}

	@Test
	void createTeamRejectsOverMaxTeamSize() throws Exception {
		HttpResponse<String> res = api.createTeam(
				"{\"name\":\"too-big\",\"dragonIds\":[\"flightmare\",\"speed_stinger\",\"night_fury\",\"armorwing\",\"timberjack\",\"deadly_nadder\",\"terrible_terror\"]}"
		);
		assertEquals(400, res.statusCode());
	}

	@Test
	void createTeamAndGetTeamNotFound() throws Exception {
		HttpResponse<String> res = api.getTeam("non-existent-team-id");
		assertEquals(404, res.statusCode());
	}

	@Test
	void createThenDeleteThenGetNotFound() throws Exception {
		HttpResponse<String> createResponse = api.createTeam("{\"name\":\"delete-me\",\"dragonIds\":[\"flightmare\"]}");
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertNotNull(teamId);
		assertFalse(teamId.isBlank());

		HttpResponse<String> deleteResponse = api.deleteTeam(teamId);
		assertEquals(200, deleteResponse.statusCode());
		assertTrue(deleteResponse.body().contains("deleted"));

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(404, getResponse.statusCode());
	}
}