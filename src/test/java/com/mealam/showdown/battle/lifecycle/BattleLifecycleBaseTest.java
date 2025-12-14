package com.mealam.showdown.battle.lifecycle;

import com.mealam.showdown.battle.BattleBaseTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class BattleLifecycleBaseTest extends BattleBaseTest {

	protected String joinBattle(HttpClient client, String battleId, String userId) throws Exception {
		HttpRequest joinRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/join"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"" + userId + "\"}"))
				.build();

		HttpResponse<String> response = client.send(joinRequest, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	protected HttpResponse<String> startBattle(HttpClient client, String battleId) throws Exception {
		HttpRequest startRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/start"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();

		return client.send(startRequest, HttpResponse.BodyHandlers.ofString());
	}

	protected HttpResponse<String> advanceTurn(HttpClient client, String battleId, String turnData) throws Exception {
		HttpRequest turnRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId + "/turn"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(turnData))
				.build();

		return client.send(turnRequest, HttpResponse.BodyHandlers.ofString());
	}

	protected HttpResponse<String> finishBattle(HttpClient client, String battleId, String winnerId) throws Exception {
		String uri = "http://localhost:" + port + "/battle/" + battleId + "/finish";
		if (winnerId != null && !winnerId.isEmpty()) {
			uri += "?winnerId=" + winnerId;
		}

		HttpRequest finishRequest = HttpRequest.newBuilder()
				.uri(new URI(uri))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();

		return client.send(finishRequest, HttpResponse.BodyHandlers.ofString());
	}

	protected String getBattleState(HttpClient client, String battleId) throws Exception {
		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId))
				.GET()
				.build();

		HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
}