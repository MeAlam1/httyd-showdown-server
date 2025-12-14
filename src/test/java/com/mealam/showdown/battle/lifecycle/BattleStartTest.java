package com.mealam.showdown.battle.lifecycle;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleStartTest extends BattleLifecycleBaseTest {

	@Test
	void startBattleWithTwoPlayers() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");

		HttpResponse<String> startResponse = startBattle(client, battleId);
		assertEquals(200, startResponse.statusCode());

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("turnNumber"));
		assertFalse(battleState.contains("-1"));
	}

	@Test
	void startBattleWithInsufficientPlayers() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		HttpResponse<String> startResponse = startBattle(client, battleId);
		assertEquals(400, startResponse.statusCode());
		assertTrue(startResponse.body().contains("Cannot start battle: no players have joined"));
	}

	@Test
	void startBattleWithOnlyOnePlayer() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		joinBattle(client, battleId, "player1");

		HttpResponse<String> startResponse = startBattle(client, battleId);
		assertEquals(400, startResponse.statusCode());
		assertTrue(startResponse.body().contains("Cannot start battle: need 2 players, but only 1 joined"));
	}

	@Test
	void startAlreadyStartedBattle() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");

		startBattle(client, battleId);

		HttpResponse<String> secondStartResponse = startBattle(client, battleId);
		assertEquals(400, secondStartResponse.statusCode());
		assertTrue(secondStartResponse.body().contains("Battle already started"));
	}

	@Test
	void verifyBattleStateAfterStart() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		joinBattle(client, battleId, "player1");
		joinBattle(client, battleId, "player2");

		startBattle(client, battleId);

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("player1"));
		assertTrue(battleState.contains("player2"));
		assertTrue(battleState.contains("turnNumber"));
	}
}