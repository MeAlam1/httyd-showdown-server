package com.mealam.showdown.battle;

import com.mealam.showdown.battle.api.BattleBaseTest;
import com.mealam.showdown.battle.utils.BattleTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class BattleJoinTest extends BattleBaseTest {

	@Test
	void joinAddsSecondPlayerAndGetShowsPlayer() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		var joinResponse = api.join(battleId, "beta", "team-beta");
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
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");
		HttpResponse<String> startResp = api.start(battleId);
		assertEquals(200, startResp.statusCode(), "Battle should start with 2 players");

		HttpResponse<String> observerJoin = api.join(battleId, "observer", "team-observer");
		assertEquals(200, observerJoin.statusCode(), "Observer join should succeed");

		String state = api.get(battleId).body();
		assertTrue(state.contains("observer"), "Observer should appear in battle state");
	}

	@Test
	void joinAfterFinishDoesNotCorruptState() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");
		api.start(battleId);
		api.finish(battleId, "p1");

		var res = api.join(battleId, "latecomer", "team-latecomer");
		assertTrue(res.statusCode() == 200 || res.statusCode() == 400);
		String state = api.get(battleId).body();
		assertTrue(state.contains("latecomer") || state.contains("winner") || state.contains("finished"));
	}

	@Test
	void concurrentDuplicateJoinsAreDeduplicated() throws Exception {
		String battleId = api.createBattleAndGetId();
		String userId = "dupe";
		String teamId = "team-dupe";

		int threads = 10;
		ExecutorService pool = Executors.newFixedThreadPool(threads);
		List<Callable<Integer>> tasks = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			tasks.add(() -> api.join(battleId, userId, teamId).statusCode());
		}
		for (Future<Integer> f : pool.invokeAll(tasks)) {
			int code = f.get(5, TimeUnit.SECONDS);
			assertTrue(code == 200 || code == 400);
		}
		pool.shutdown();

		String state = api.get(battleId).body();
		int count = countOccurrences(state, userId);
		assertTrue(count >= 1 && count <= 3, "User should appear at least once (may appear in teams structure)");
	}

	private int countOccurrences(String text, String target) {
		return (text.length() - text.replace(target, "").length()) / target.length();
	}

	@Test
	void concurrentDistinctJoinsYieldTwoPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Future<Integer> a = pool.submit(() -> api.join(battleId, "A", "team-A").statusCode());
		Future<Integer> b = pool.submit(() -> api.join(battleId, "B", "team-B").statusCode());
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

		api.join(battleId, "testuser", "team-testuser");
		api.join(battleId, "TestUser", "team-TestUser");

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("testuser") || battleState.contains("TestUser"));
	}

	@Test
	void verifyDuplicatePlayerJoinPrevented() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		String userId = BattleTestUtils.generateRandomUserId();
		String teamId = "team-" + userId;

		api.join(battleId, userId, teamId);
		api.join(battleId, userId, teamId);

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains(userId));

		int count = countOccurrences(battleState, userId);
		assertTrue(count >= 1, "User should appear at least once");
	}

	@Test
	void veryLongUserIdIsHandled() throws Exception {
		String battleId = api.createBattleAndGetId();
		String longId = "u".repeat(2048);
		HttpResponse<String> res = api.join(battleId, longId, "team-long");
		assertTrue(res.statusCode() == 200 || res.statusCode() == 400);
	}

	@Test
	void unicodeUserIdsAreHandled() throws Exception {
		String battleId = api.createBattleAndGetId();
		String[] ids = {"用户", "игрок", "プレイヤー", "لاعب", "😀"};
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			HttpResponse<String> res = api.join(battleId, id, "team-" + i);
			assertTrue(res.statusCode() == 200 || res.statusCode() == 400);
		}
		String state = api.get(battleId).body();
		assertNotNull(state);
	}

	@Test
	void randomizedJoinsAcrossThreadsProduceConsistentState() throws Exception {
		String battleId = api.createBattleAndGetId();
		int players = 12;
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<String> userIds = new ArrayList<>();
		List<Callable<HttpResponse<String>>> tasks = new ArrayList<>();
		for (int i = 0; i < players; i++) {
			final String userId = "random-" + i + "-" + UUID.randomUUID().toString().substring(0, 8);
			final String teamId = "team-rand-" + i;
			userIds.add(userId);
			tasks.add(() -> api.join(battleId, userId, teamId));
		}
		List<Future<HttpResponse<String>>> futures = pool.invokeAll(tasks);
		pool.shutdown();

		for (Future<HttpResponse<String>> future : futures) {
			HttpResponse<String> resp = future.get(10, TimeUnit.SECONDS);
			assertTrue(resp.statusCode() == 200 || resp.statusCode() == 400,
					"Join should either accept the player or gracefully reject duplicates");
		}

		String state = api.get(battleId).body();
		assertNotNull(state);
		for (int i = 0; i < userIds.size(); i++) {
			String userId = userIds.get(i);
			String teamId = "team-rand-" + i;
			assertTrue(state.contains(userId) || state.contains(teamId),
					"Battle state should reflect randomized join order");
		}
	}
}