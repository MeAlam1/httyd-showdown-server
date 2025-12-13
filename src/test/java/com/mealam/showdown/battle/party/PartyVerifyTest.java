package com.mealam.showdown.battle.party;

import com.mealam.showdown.battle.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class PartyVerifyTest extends BattleBaseTest {

	@Test
	void verifyPartyAfterJoin() throws Exception {
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

		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId))
				.GET()
				.build();

		HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("beta"));
	}

	@Test
	void verifyPartyAfterLeave() throws Exception {
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

		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId))
				.GET()
				.build();

		HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("beta"));

		HttpRequest leaveRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/leave"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"beta\"}"))
				.build();

		HttpResponse<String> leaveResponse = client.send(leaveRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, leaveResponse.statusCode());

		HttpResponse<String> verifyLeaveResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, verifyLeaveResponse.statusCode());
		assertFalse(verifyLeaveResponse.body().contains("beta"));
	}
}