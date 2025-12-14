package com.mealam.showdown.battle;

import com.mealam.showdown.Main;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BattleBaseTest {

	protected Javalin app;
	protected int port;

	@BeforeEach
	void setup() {
		app = Main.createApp();
		app.start(7071);
		port = app.port();
	}

	@AfterEach
	void teardown() {
		app.stop();
	}

	protected String createBattleAndGetId(HttpClient client) throws Exception {
		HttpRequest createRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:" + port + "/battle/create"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{}"))
				.build();

		HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
		String body = createResponse.body();
		return extractBattleIdFromBody(body);
	}

	protected static String extractBattleIdFromBody(String body) {
		Pattern p = Pattern.compile("\"id\"\\s*:\\s*\"([A-Za-z0-9_-]+)\"");
		Matcher m = p.matcher(body);
		if (m.find()) return m.group(1);
		throw new IllegalStateException("Could not extract id from response: " + body);
	}

	protected record TestWebSocketListener(CountDownLatch openLatch) implements WebSocket.Listener {

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