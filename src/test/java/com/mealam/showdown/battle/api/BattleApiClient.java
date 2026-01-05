/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.api;

import com.mealam.showdown.battle.utils.BattleTestUtils;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;

public class BattleApiClient {

	private final int port;
	private final HttpClient client;

	public BattleApiClient(int pPort, HttpClient pClient) {
		this.port = pPort;
		this.client = pClient;
	}

	public String baseUrl() {
		return "http://localhost:" + port;
	}

	public String wsBaseUrl() {
		return "ws://localhost:" + port;
	}

	public HttpResponse<String> createBattle(String pJsonBody) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/create"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(pJsonBody == null ? "{}" : pJsonBody))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public String createBattleAndGetId() throws Exception {
		HttpResponse<String> res = createBattle("{}");
		String body = res.body();
		return BattleTestUtils.extractBattleIdFromBody(body);
	}

	public HttpResponse<String> join(String pBattleId, String pUserId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/" + pBattleId + "/join"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"" + pUserId + "\"}"))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	// TODO: Once the amount of JSON data for this API grows, consider using Resource Loading like with Team Tests
	public HttpResponse<String> join(String pBattleId, String pUserId, String pTeamId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/" + pBattleId + "/join"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"" + pUserId + "\",\"teamId\":\"" + pTeamId + "\"}"))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> leave(String pBattleId, String pUserId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/" + pBattleId + "/leave"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"userId\":\"" + pUserId + "\"}"))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> start(String pBattleId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/" + pBattleId + "/start"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> turn(String pBattleId, String pUserId, String pTurnData) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/" + pBattleId + "/turn"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + pUserId)
				.POST(HttpRequest.BodyPublishers.ofString(pTurnData == null ? "{}" : pTurnData))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> finish(String pBattleId, String pWinnerId) throws Exception {
		String url = baseUrl() + "/api/battle/" + pBattleId + "/finish";
		if (pWinnerId != null && !pWinnerId.isEmpty()) {
			url += "?winnerId=" + URLEncoder.encode(pWinnerId, StandardCharsets.UTF_8);
		}
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> get(String pBattleId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/battle/" + pBattleId))
				.GET()
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public WebSocket connectBattleWebSocket(String pBattleId, WebSocket.Listener pListener) {
		return client.newWebSocketBuilder()
				.buildAsync(URI.create(wsBaseUrl() + "/ws/battle/" + pBattleId), pListener)
				.join();
	}
}
