package com.mealam.showdown.battle.lifecycle;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleProgressTest extends BattleLifecycleBaseTest {

	@Test
	void advanceTurnInActiveBattle() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);

		String turnData = "{\"turnNumber\":1,\"actions\":[]}";
		HttpResponse<String> turnResponse = advanceTurn(client, battleId, turnData);
		assertEquals(200, turnResponse.statusCode());

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("turnNumber"));
	}

	@Test
	void advanceTurnBeforeBattleStart() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");

		String turnData = "{\"turnNumber\":1,\"actions\":[]}";
		HttpResponse<String> turnResponse = advanceTurn(client, battleId, turnData);
		assertEquals(400, turnResponse.statusCode());
		assertTrue(turnResponse.body().contains("Battle not started"));
	}

	@Test
	void advanceMultipleTurns() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);

		for (int i = 1; i <= 3; i++) {
			String turnData = "{\"turnNumber\":" + i + ",\"actions\":[]}";
			HttpResponse<String> turnResponse = advanceTurn(client, battleId, turnData);
			assertEquals(200, turnResponse.statusCode());
		}

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("turnNumber"));
	}

	@Test
	void advanceTurnAfterBattleFinish() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");
		startBattle(client, battleId);
		finishBattle(client, battleId, "player1");

		String turnData = "{\"turnNumber\":1,\"actions\":[]}";
		HttpResponse<String> turnResponse = advanceTurn(client, battleId, turnData);
		assertEquals(400, turnResponse.statusCode());
		assertTrue(turnResponse.body().contains("Battle already finished"));
	}

	@Test
	void advanceTurnInNonExistentBattle() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String turnData = "{\"turnNumber\":1,\"actions\":[]}";
		HttpResponse<String> turnResponse = advanceTurn(client, "nonexistent-id", turnData);
		assertEquals(404, turnResponse.statusCode());
		assertTrue(turnResponse.body().contains("Battle not found"));
	}
}