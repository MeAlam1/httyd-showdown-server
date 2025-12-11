package com.mealam.showdown.battle;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class BattleRouterTest {

	private Javalin app;
	private int port;

	@BeforeEach
	void setup() {
		app = Javalin.create();
		app.exception(Exception.class, (e, ctx) -> {
			e.printStackTrace();
			ctx.status(500).result(e.getMessage());
		});
		BattleRouter.register(app);
		app.start(7071);
		port = app.port();
	}

	@AfterEach
	void teardown() {
		app.stop();
	}

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

	@Test
	void joinBattle() throws Exception {
		createBattle();

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
		assertTrue(joinResponse.body().contains("id"));

		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/" + battleId))
				.GET()
				.build();

		HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("beta"));
	}

	private String createBattleAndGetId(HttpClient client) throws Exception {
		HttpRequest createRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/create"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"playerIds\":[\"alpha\"]}"))
				.build();

		HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
		String body = createResponse.body();
		return extractBattleIdFromBody(body);
	}

	private static String extractBattleIdFromBody(String body) {
		Pattern p = Pattern.compile("\"id\"\\s*:\\s*\"([A-Za-z0-9_-]+)\"");
		Matcher m = p.matcher(body);
		if (m.find()) return m.group(1);
		throw new IllegalStateException("Could not extract id from response: " + body);
	}

	private record TestWebSocketListener(CountDownLatch openLatch) implements WebSocket.Listener {

		@Override
		public void onOpen(WebSocket webSocket) {
			openLatch.countDown();
			webSocket.request(1);
		}

		@Override
		public void onError(WebSocket webSocket, Throwable error) {
			fail("WebSocket error: " + error.getMessage());
		}
	}
}