package com.mealam.showdown.battle.api;

import com.mealam.showdown.BaseTest;
import com.mealam.showdown.Main;
import org.junit.jupiter.api.BeforeEach;

import java.net.http.WebSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BattleBaseTest extends BaseTest {

	protected BattleApiClient api;

	@BeforeEach
	void setupBattle() {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");
			Files.createDirectories(dataRoot.resolve("battles"));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare battles dir", e);
		}

		api = new BattleApiClient(port, client);
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