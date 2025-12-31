package com.mealam.showdown.team;

import com.mealam.showdown.team.api.TeamBaseTest;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class TeamCreateTest extends TeamBaseTest {

	@Override
	protected String loadResource(String pResourcePath) throws IOException {
		return super.loadResource("create/" + pResourcePath);
	}

	@Test
	void createTeam() throws Exception {
		String payload = loadResource("createTeam.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());
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
		String payload = loadResource("createTeamRejectsMissingName.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(400, createResponse.statusCode());
	}

	@Test
	void createTeamRejectsBlankName() throws Exception {
		String payload = loadResource("createTeamRejectsBlankName.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(400, createResponse.statusCode());
	}

	@Test
	void createTeamAllowsMissingDragonIds() throws Exception {
		String payload = loadResource("createTeamAllowsMissingDragonIds.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		assertFalse(teamId.isBlank());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("no-dragons"));
	}

	@Test
	void createTeamRejectsUnknownDragonId() throws Exception {
		String payload = loadResource("createTeamRejectsUnknownDragonId.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(400, createResponse.statusCode());
	}

	@Test
	void createTeamTrimsDragonIds() throws Exception {
		String payload = loadResource("createTeamTrimsDragonIds.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("trim-team"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void createTeamIgnoresBlankAndNullLikeDragonIds() throws Exception {
		String payload = loadResource("createTeamIgnoresBlankAndNullLikeDragonIds.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(201, createResponse.statusCode());

		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());
		HttpResponse<String> getResponse = api.getTeam(teamId);

		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("ignore-blanks"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void createTeamDeduplicatesDragonIds() throws Exception {
		String payload = loadResource("createTeamDeduplicatesDragonIds.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
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
		String payload = loadResource("createTeamRejectsOverMaxTeamSize.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
		assertEquals(400, createResponse.statusCode());
	}

	@Test
	void createTeamAndGetTeamNotFound() throws Exception {
		HttpResponse<String> res = api.getTeam("non-existent-team-id");
		assertEquals(404, res.statusCode());
	}

	@Test
	void createThenDeleteThenGetNotFound() throws Exception {
		String payload = loadResource("createThenDeleteThenGetNotFound.json");

		HttpResponse<String> createResponse = api.createTeam(payload);
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