package com.mealam.showdown.battle.lifecycle;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleFinishTest extends BattleBaseTest {

	@Test
	void finishBattleWithWinner() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1");
		api.join(battleId, "player2");
		api.start(battleId);

		HttpResponse<String> finishResponse = api.finish(battleId, "player1");
		assertEquals(200, finishResponse.statusCode());

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("player1"));
		assertTrue(battleState.contains("winner"));
	}

	@Test
	void finishBattleWithoutWinner() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1");
		api.join(battleId, "player2");
		api.start(battleId);

		HttpResponse<String> finishResponse = api.finish(battleId, null);
		assertEquals(200, finishResponse.statusCode());

		String battleState = api.get(battleId).body();
		assertNotNull(battleState);
	}

	@Test
	void finishBattleBeforeStart() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1");
		api.join(battleId, "player2");

		HttpResponse<String> finishResponse = api.finish(battleId, "player1");
		assertEquals(200, finishResponse.statusCode());
	}

	@Test
	void finishNonExistentBattle() throws Exception {
		HttpResponse<String> finishResponse = api.finish("nonexistent-id", "player1");
		assertEquals(404, finishResponse.statusCode());
		assertTrue(finishResponse.body().contains("Battle not found"));
	}

	@Test
	void verifyTurnManagerCleanupAfterFinish() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1");
		api.join(battleId, "player2");
		api.start(battleId);
		api.finish(battleId, "player1");

		String turnData = "{\"turnNumber\":1,\"actions\":[]}";
		HttpResponse<String> turnResponse = api.turn(battleId, turnData);
		assertEquals(400, turnResponse.statusCode());
	}

	@Test
	void finishBattleMultipleTimes() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1");
		api.join(battleId, "player2");
		api.start(battleId);

		api.finish(battleId, "player1");
		HttpResponse<String> secondFinish = api.finish(battleId, "player2");
		assertEquals(200, secondFinish.statusCode());
	}

	@Test
	void finishWithUrlEncodedWinner() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1");
		api.join(battleId, "p 2 🚀");
		api.start(battleId);

		HttpResponse<String> res = api.finish(battleId, "p 2 🚀");
		assertEquals(200, res.statusCode());
	}
}