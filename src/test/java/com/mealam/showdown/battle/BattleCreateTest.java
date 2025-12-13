package com.mealam.showdown.battle;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BattleCreateTest extends BattleBaseTest {

	@Test
	void createBattle() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest createRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/create"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"playerIds\":[\"alpha\"]}"))
				.build();

		HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, createResponse.statusCode());

		String body = createResponse.body();
		assertNotNull(body);
		assertTrue(body.contains("id"));

		String battleId = extractBattleIdFromBody(body);
		assertFalse(battleId.isEmpty());

		CountDownLatch latch = new CountDownLatch(1);
		TestWebSocketListener listener = new TestWebSocketListener(latch);

		WebSocket ws = client.newWebSocketBuilder()
				.buildAsync(URI.create("ws://localhost:" + port + "/ws/battle/" + battleId), listener)
				.join();

		assertNotNull(ws);
		latch.await();

		ws.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
	}
}