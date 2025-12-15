package com.mealam.showdown.battle;

import com.mealam.showdown.battle.api.BattleApiClient;
import com.mealam.showdown.battle.api.BattleBaseTest;
import com.mealam.showdown.battle.utils.BattleTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class BattleCreateTest extends BattleBaseTest {

	@Test
	void createBattle() throws Exception {
		var createResponse = api.createBattle("{\"playerIds\":[\"alpha\"]}");
		assertEquals(201, createResponse.statusCode());

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

	@Test
	void createsManyBattlesWithUniqueIds() throws Exception {
		int n = 25;
		ExecutorService pool = Executors.newFixedThreadPool(8);
		List<Callable<String>> tasks = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			tasks.add(() -> api.createBattleAndGetId());
		}
		List<Future<String>> futures = pool.invokeAll(tasks);
		pool.shutdown();

		List<String> ids = new ArrayList<>();
		for (Future<String> f : futures) {
			String id = f.get(10, TimeUnit.SECONDS);
			assertNotNull(id);
			assertFalse(id.isBlank());
			assertTrue(id.matches("[A-Za-z0-9_-]+"));
			ids.add(id);

			assertEquals(200, api.get(id).statusCode());
		}

		long distinct = ids.stream().distinct().count();
		assertEquals(ids.size(), distinct, "Battle ids should be unique");
	}
}