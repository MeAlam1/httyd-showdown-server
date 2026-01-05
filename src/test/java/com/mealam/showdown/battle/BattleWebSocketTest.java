/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

public class BattleWebSocketTest extends BattleBaseTest {

	@Test
	void multipleWebSocketConnectAndClose() throws Exception {
		String battleId = api.createBattleAndGetId();

		int n = 5;
		CountDownLatch openLatch = new CountDownLatch(n);
		List<WebSocket> sockets = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			BattleBaseTest.TestWebSocketListener listener = new BattleBaseTest.TestWebSocketListener(openLatch);
			WebSocket ws = api.connectBattleWebSocket(battleId, listener);
			assertNotNull(ws);
			sockets.add(ws);
		}

		openLatch.await();

		for (WebSocket ws : sockets) {
			ws.sendClose(WebSocket.NORMAL_CLOSURE, "bye").join();
		}
	}
}
