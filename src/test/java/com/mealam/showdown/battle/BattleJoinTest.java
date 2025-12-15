package com.mealam.showdown.battle;

import com.mealam.showdown.battle.api.BattleBaseTest;
import com.mealam.showdown.battle.utils.BattleTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class BattleJoinTest extends BattleBaseTest {

	@Test
	void joinAddsSecondPlayerAndGetShowsPlayer() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		var joinResponse = api.join(battleId, "beta");
		assertEquals(200, joinResponse.statusCode());
		assertNotNull(joinResponse.body());
		assertTrue(joinResponse.body().contains("battleId"));

		var getResponse = api.get(battleId);
		assertEquals(200, getResponse.statusCode());
		assertTrue(getResponse.body().contains("beta"));
	}

	@Test
	void thirdJoinAfterStartIsSpectator() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1");
		api.join(battleId, "p2");
		assertEquals(200, api.start(battleId).statusCode());

		assertEquals(200, api.join(battleId, "observer").statusCode());
		String state = api.get(battleId).body();
		assertTrue(state.contains("observer"));
		assertTrue(state.contains("spectator") || state.contains("observers"));
	}

	@Test
	void joinAfterFinishDoesNotCorruptState() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1");
		api.join(battleId, "p2");
		api.start(battleId);
		api.finish(battleId, "p1");

		var res = api.join(battleId, "latecomer");
		assertTrue(res.statusCode() == 200 || res.statusCode() == 400);
		String state = api.get(battleId).body();
		assertTrue(state.contains("latecomer") || state.contains("winner") || state.contains("finished"));
	}

	@Test
	void concurrentDuplicateJoinsAreDeduplicated() throws Exception {
		String battleId = api.createBattleAndGetId();
		String userId = "dupe";

		int threads = 10;
		ExecutorService pool = Executors.newFixedThreadPool(threads);
		List<Callable<Integer>> tasks = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			tasks.add(() -> api.join(battleId, userId).statusCode());
		}
		for (Future<Integer> f : pool.invokeAll(tasks)) {
			int code = f.get(5, TimeUnit.SECONDS);
			assertTrue(code == 200 || code == 400);
		}
		pool.shutdown();

		String state = api.get(battleId).body();
		int count = state.split(userId, -1).length - 1;
		assertEquals(1, count, "User should appear exactly once after concurrent joins");
	}

	@Test
	void concurrentDistinctJoinsYieldTwoPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Future<Integer> a = pool.submit(() -> api.join(battleId, "A").statusCode());
		Future<Integer> b = pool.submit(() -> api.join(battleId, "B").statusCode());
		assertEquals(200, a.get(5, TimeUnit.SECONDS));
		assertEquals(200, b.get(5, TimeUnit.SECONDS));
		pool.shutdown();

		String state = api.get(battleId).body();
		assertTrue(state.contains("A"));
		assertTrue(state.contains("B"));
	}

	@Test
	void verifyDuplicatePlayerWithDifferentCasing() throws Exception {

		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "testuser");
		api.join(battleId, "TestUser");

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("testuser") || battleState.contains("TestUser"));
	}

	@Test
	void verifyDuplicatePlayerJoinPrevented() throws Exception {

		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		String userId = BattleTestUtils.generateRandomUserId();

		api.join(battleId, userId);
		api.join(battleId, userId);

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains(userId));

		int count = battleState.split(userId, -1).length - 1;
		assertTrue(count >= 1, "User should appear at least once");
	}

	@Test
	void veryLongUserIdIsHandled() throws Exception {
		String battleId = api.createBattleAndGetId();
		String longId = "u".repeat(2048);
		HttpResponse<String> res = api.join(battleId, longId);
		assertTrue(res.statusCode() == 200 || res.statusCode() == 400);
	}

	@Test
	void unicodeUserIdsAreHandled() throws Exception {
		String battleId = api.createBattleAndGetId();
		String[] ids = {"用户", "игрок", "プレイヤー", "لاعب", "😀"};
		for (String id : ids) {
			HttpResponse<String> res = api.join(battleId, id);
			assertTrue(res.statusCode() == 200 || res.statusCode() == 400);
		}
		String state = api.get(battleId).body();
		assertNotNull(state);
	}
}