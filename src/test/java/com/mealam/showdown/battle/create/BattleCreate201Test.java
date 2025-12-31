package com.mealam.showdown.battle.create;

import com.mealam.showdown.battle.api.BattleBaseTest;
import com.mealam.showdown.battle.utils.BattleTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BattleCreate201Test extends BattleBaseTest {

	private static final int STATUS_CODE = 201;

	@Test
	void shouldCreateBattleSuccessfully() throws Exception {
		String payload = loadResource("OnePlayer");
		HttpResponse<String> createResponse = api.createBattle(payload);
		assertEquals(STATUS_CODE, createResponse.statusCode());

		String body = createResponse.body();
		assertNotNull(body);
		assertTrue(body.contains("battleId"));

		String battleId = BattleTestUtils.extractBattleIdFromBody(body);
		assertFalse(battleId.isEmpty());

		CountDownLatch latch = new CountDownLatch(1);
		TestWebSocketListener listener = new TestWebSocketListener(latch);

		WebSocket ws = api.connectBattleWebSocket(battleId, listener);
		assertNotNull(ws);

		latch.await();
		ws.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
	}
}