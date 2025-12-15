package com.mealam.showdown.battle.api;

import com.mealam.showdown.Main;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BattleBaseTest {

	protected Javalin app;
	protected int port;
	protected HttpClient client;
	protected BattleApiClient api;

	@BeforeEach
	void setup() {
		app = Main.createApp();
		app.start(0);
		port = app.port();
		client = HttpClient.newHttpClient();
		api = new BattleApiClient(port, client);
	}

	@AfterEach
	void teardown() {
		if (app != null) {
			try {
				app.stop();
			} finally {
				app = null;
			}
		}
	}

	public record TestWebSocketListener(CountDownLatch openLatch) implements WebSocket.Listener {
		@Override
		public void onOpen(WebSocket pWebSocket) {
			openLatch.countDown();
			pWebSocket.request(1);
		}

		@Override
		public void onError(WebSocket pWebSocket, Throwable pError) {
			fail("WebSocket error: " + pError.getMessage());
		}
	}
}