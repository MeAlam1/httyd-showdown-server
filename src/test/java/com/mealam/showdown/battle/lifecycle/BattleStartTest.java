package com.mealam.showdown.battle.lifecycle;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleStartTest extends BattleBaseTest {

	@Test
	void startBattleWithTwoPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1");
		api.join(battleId, "player2");

		HttpResponse<String> startResponse = api.start(battleId);
		assertEquals(200, startResponse.statusCode());

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("turnNumber"));
		assertFalse(battleState.contains("-1"));
	}

	@Test
	void startBattleWithInsufficientPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		HttpResponse<String> startResponse = api.start(battleId);
		assertEquals(400, startResponse.statusCode());
		assertTrue(startResponse.body().contains("Cannot start battle: no players have joined"));
	}

	@Test
	void startBattleWithOnlyOnePlayer() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1");

		HttpResponse<String> startResponse = api.start(battleId);
		assertEquals(400, startResponse.statusCode());
		assertTrue(startResponse.body().contains("Cannot start battle: need 2 players, but only 1 joined"));
	}

	@Test
	void startAlreadyStartedBattle() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1");
		api.join(battleId, "player2");

		api.start(battleId);

		HttpResponse<String> secondStartResponse = api.start(battleId);
		assertEquals(400, secondStartResponse.statusCode());
		assertTrue(secondStartResponse.body().contains("Battle already started"));
	}

	@Test
	void verifyBattleStateAfterStart() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1");
		api.join(battleId, "player2");

		api.start(battleId);

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("player1"));
		assertTrue(battleState.contains("player2"));
		assertTrue(battleState.contains("turnNumber"));
	}
}