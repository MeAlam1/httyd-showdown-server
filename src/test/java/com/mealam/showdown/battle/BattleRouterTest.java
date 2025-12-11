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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

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
	void completeBattleFlow() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest createRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:" + port + "/battle/start")).header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"playerId\":\"alpha\"}")).build();

		HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, createResponse.statusCode());

		String body = createResponse.body();
		assertNotNull(body);
		assertTrue(body.contains("id"));

		String battleId = body.replaceAll("[^0-9A-Za-z_-]", "");
		assertFalse(battleId.isEmpty());

		CountDownLatch latch = new CountDownLatch(1);
		TestWebSocketListener listener = new TestWebSocketListener(latch);

		WebSocket ws = client.newWebSocketBuilder().buildAsync(URI.create("ws://localhost:" + port + "/ws/battle/" + battleId), listener).join();

		assertNotNull(ws);
		latch.await();
	
	  /*
	  CompletableFuture<Void> joinSent = ws.sendText("{\"action\":\"join\",\"name\":\"Player1\"}", true);
	  joinSent.join();
	  Thread.sleep(100);
	
	  CompletableFuture<Void> leaveSent = ws.sendText("{\"action\":\"leave\"}", true);
	  leaveSent.join();
	  Thread.sleep(100);*/

		ws.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
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