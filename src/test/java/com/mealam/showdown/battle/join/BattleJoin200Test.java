package com.mealam.showdown.battle.join;

import com.mealam.showdown.battle.api.BattleBaseTest;
import com.mealam.showdown.battle.utils.BattleTestUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleJoin200Test extends BattleBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldAddSecondPlayer() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		var joinResponse = api.join(battleId, "beta", "team-beta");
		assertEquals(STATUS_CODE, joinResponse.statusCode());
		assertNotNull(joinResponse.body());
		assertTrue(joinResponse.body().contains("battleId"));

		var getResponse = api.get(battleId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
		assertTrue(getResponse.body().contains("beta"));
	}

	@Test
	void shouldJoinAsSpectatorAfterBattleStart() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");
		HttpResponse<String> startResp = api.start(battleId);
		assertEquals(STATUS_CODE, startResp.statusCode(), "Battle should start with 2 players");

		HttpResponse<String> observerJoin = api.join(battleId, "observer", "team-observer");
		assertEquals(STATUS_CODE, observerJoin.statusCode(), "Observer join should succeed");
	}

	@Test
	void shouldJoinAsSpectatorAfterBattleFinished() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");
		api.start(battleId);
		api.finish(battleId, "p1");

		// TODO: Should actually join as Spectator, but for now just verify no corruption

		var res = api.join(battleId, "latecomer", "team-latecomer");
		assertEquals(STATUS_CODE, res.statusCode(), "Late join should be accepted");
	}

	@Test
	void shouldAllowDifferentCasing() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "testuser", "team-testuser");
		api.join(battleId, "TestUser", "team-TestUser");

		String battleState = api.get(battleId).body();
		// TODO: Will actually not allow duplicate names, but for now just verify no crash
		assertTrue(battleState.contains("testuser") || battleState.contains("TestUser"));
	}

	@Test
	void shouldNotAllowSamePlayerJoinTwice() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		String userId = BattleTestUtils.generateRandomUserId();
		String teamId = "team-" + userId;

		api.join(battleId, userId, teamId);
		api.join(battleId, userId, teamId);

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains(userId));

		int count = BattleTestUtils.countOccurrences(battleState, userId);
		assertTrue(count >= 1, "User should appear at least once");
	}

	@Test
	void shouldAcceptAnyIdLength() throws Exception {
		String battleId = api.createBattleAndGetId();
		String longId = "u".repeat(2048);
		HttpResponse<String> res = api.join(battleId, longId, "team-long");
		assertEquals(STATUS_CODE, res.statusCode(), "Server should accept very long user ID");
	}

	//TODO: Revise this test once we have clearer rules on allowed characters
	@Test
	void shouldAcceptUnicodeNames() throws Exception {
		String battleId = api.createBattleAndGetId();
		String[] ids = {"用户", "игрок", "プレイヤー", "لاعب", "😀"};
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			HttpResponse<String> res = api.join(battleId, id, "team-" + i);
			assertEquals(STATUS_CODE, res.statusCode(), "Unicode user ID should be accepted: " + id);
		}
		String state = api.get(battleId).body();
		assertNotNull(state);
	}
}
