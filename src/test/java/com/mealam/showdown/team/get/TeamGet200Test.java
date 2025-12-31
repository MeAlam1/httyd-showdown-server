package com.mealam.showdown.team.get;

import com.mealam.showdown.team.api.TeamBaseTest;
import com.mealam.showdown.team.utils.TeamTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeamGet200Test extends TeamBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldGetTeamAfterCreation() throws Exception {
		String payload = loadResource("singleFullDragon");
		HttpResponse<String> createResponse = api.createTeam(payload);
		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
		assertTrue(getResponse.body().contains("alpha-team"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void shouldGetTeamWithNoDragons() throws Exception {
		String payload = loadResource("onlyTeamName");
		HttpResponse<String> createResponse = api.createTeam(payload);
		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
		assertTrue(getResponse.body().contains("no-dragons"));
	}

	@Test
	void shouldGetTrimmedTeam() throws Exception {
		String payload = loadResource("spacesInDragonId");
		HttpResponse<String> createResponse = api.createTeam(payload);
		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
		assertTrue(getResponse.body().contains("trim-team"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void shouldGetTeamIgnoringBlankDragonIds() throws Exception {
		String payload = loadResource("blankDragonIds");
		HttpResponse<String> createResponse = api.createTeam(payload);
		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
		assertTrue(getResponse.body().contains("ignore-blanks"));
		assertTrue(getResponse.body().contains("flightmare"));
	}

	@Test
	void shouldGetTeamWithDeduplicatedDragonIds() throws Exception {
		String payload = loadResource("sameDragonIds");
		HttpResponse<String> createResponse = api.createTeam(payload);
		String teamId = TeamTestUtils.extractTeamIdFromBody(createResponse.body());

		HttpResponse<String> getResponse = api.getTeam(teamId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
		assertTrue(getResponse.body().contains("dupe-team"));

		String responseBody = getResponse.body();
		int first = responseBody.indexOf("flightmare");
		int last = responseBody.lastIndexOf("flightmare");
		assertTrue(first >= 0);
		assertEquals(first, last);
	}
}