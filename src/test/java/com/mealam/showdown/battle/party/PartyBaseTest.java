package com.mealam.showdown.battle.party;

import com.mealam.showdown.battle.BattleBaseTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public abstract class PartyBaseTest extends BattleBaseTest {

	protected String joinBattle(HttpClient client, String battleId, String userId) throws Exception {
		HttpRequest joinRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/join"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"" + userId + "\"}"))
				.build();

		HttpResponse<String> response = client.send(joinRequest, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	protected String leaveBattle(HttpClient client, String battleId, String userId) throws Exception {
		HttpRequest leaveRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/leave"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"" + userId + "\"}"))
				.build();

		HttpResponse<String> response = client.send(leaveRequest, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	protected String getBattleState(HttpClient client, String battleId) throws Exception {
		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId))
				.GET()
				.build();

		HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	protected String generateRandomUserId() {
		return "user_" + UUID.randomUUID().toString().substring(0, 8);
	}
}