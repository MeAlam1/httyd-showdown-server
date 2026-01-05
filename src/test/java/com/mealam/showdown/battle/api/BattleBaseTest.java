/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.api;

import static org.junit.jupiter.api.Assertions.fail;

import com.mealam.showdown.BaseTest;
import com.mealam.showdown.Main;
import java.io.IOException;
import java.net.http.WebSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.BeforeEach;

public abstract class BattleBaseTest extends BaseTest {

	protected BattleApiClient api;

	@Override
	protected String loadResource(String pResourcePath) throws IOException {
		return super.loadResource("battle/" + pResourcePath);
	}

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
