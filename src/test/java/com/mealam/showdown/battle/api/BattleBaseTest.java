package com.mealam.showdown.battle.api;

import com.mealam.showdown.Main;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BattleBaseTest {

	protected Javalin app;
	protected int port;
	protected HttpClient client;
	protected BattleApiClient api;

	@BeforeEach
	void setup() {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");

			Files.createDirectories(dataRoot);
			Files.createDirectories(dataRoot.resolve("battles"));
			System.setProperty("showdown.data.dir", dataRoot.toString());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare data dir", e);
		}

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