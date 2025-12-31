package com.mealam.showdown.team.get;

import com.mealam.showdown.team.api.TeamBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamGet404Test extends TeamBaseTest {

	private static final int STATUS_CODE = 404;

	@ParameterizedTest
	@ValueSource(strings = {"", "does-not-exist", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
	void shouldReturn404ForInvalidTeamIds(String pTeamId) throws Exception {
		HttpResponse<String> response = api.getTeam(pTeamId);
		assertEquals(STATUS_CODE, response.statusCode());
	}
}