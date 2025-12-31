package com.mealam.showdown.team.create;

import com.mealam.showdown.team.api.TeamBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamCreate400Test extends TeamBaseTest {

	private static final int STATUS_CODE = 400;

	@ParameterizedTest
	@ValueSource(strings = {"noTeamName", "blankTeamName", "unknownDragonId", "tooManyDragons"})
	void shouldRejectInvalidPayloads(String pResourceName) throws Exception {
		String payload = loadResource(pResourceName);
		HttpResponse<String> response = api.createTeam(payload);
		assertEquals(STATUS_CODE, response.statusCode());
	}
}