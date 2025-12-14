package com.mealam.showdown.battle;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleJoinTest extends BattleBaseTest {

	@Test
	void joinBattle() throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		
		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		HttpRequest joinRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/join"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"beta\"}"))
				.build();

		HttpResponse<String> joinResponse = client.send(joinRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, joinResponse.statusCode());
		assertNotNull(joinResponse.body());
		assertTrue(joinResponse.body().contains("battleId"));

		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId))
				.GET()
				.build();

		HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("beta"));
	}
}