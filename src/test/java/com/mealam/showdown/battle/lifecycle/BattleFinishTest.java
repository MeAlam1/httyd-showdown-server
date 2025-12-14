package com.mealam.showdown.battle.lifecycle;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleFinishTest extends BattleLifecycleBaseTest {

	@Test
	void finishBattleWithWinner() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);

		HttpResponse<String> finishResponse = finishBattle(client, battleId, "player1");
		assertEquals(200, finishResponse.statusCode());

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("player1"));
		assertTrue(battleState.contains("winner"));
	}

	@Test
	void finishBattleWithoutWinner() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);

		HttpResponse<String> finishResponse = finishBattle(client, battleId, null);
		assertEquals(200, finishResponse.statusCode());

		String battleState = getBattleState(client, battleId);
		assertNotNull(battleState);
	}

	@Test
	void finishBattleBeforeStart() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");

		HttpResponse<String> finishResponse = finishBattle(client, battleId, "player1");
		assertEquals(200, finishResponse.statusCode());
	}

	@Test
	void finishNonExistentBattle() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> finishResponse = finishBattle(client, "nonexistent-id", "player1");
		assertEquals(404, finishResponse.statusCode());
		assertTrue(finishResponse.body().contains("Battle not found"));
	}

	@Test
	void verifyTurnManagerCleanupAfterFinish() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);
		finishBattle(client, battleId, "player1");

		String turnData = "{\"turnNumber\":1,\"actions\":[]}";
		HttpResponse<String> turnResponse = advanceTurn(client, battleId, turnData);
		assertEquals(400, turnResponse.statusCode());
	}

	@Test
	void finishBattleMultipleTimes() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);

		finishBattle(client, battleId, "player1");
		HttpResponse<String> secondFinish = finishBattle(client, battleId, "player2");
		assertEquals(200, secondFinish.statusCode());
	}
}