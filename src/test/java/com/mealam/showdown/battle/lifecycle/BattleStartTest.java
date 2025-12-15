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

		HttpResponse<String> join1 = api.join(battleId, "player1", "team-player1");
		assertEquals(200, join1.statusCode(), "First join should succeed");

		HttpResponse<String> join2 = api.join(battleId, "player2", "team-player2");
		assertEquals(200, join2.statusCode(), "Second join should succeed");

		HttpResponse<String> startResponse = api.start(battleId);
		assertEquals(200, startResponse.statusCode(), "Start should succeed with 2 players");

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("turnNumber") || battleState.contains("\"turnContext\""));
		assertFalse(battleState.contains("\"-1\""));
	}

	@Test
	void startBattleWithInsufficientPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		HttpResponse<String> startResponse = api.start(battleId);
		assertTrue(startResponse.statusCode() >= 400 && startResponse.statusCode() < 500);
		assertTrue(startResponse.body().contains("Cannot start battle") || startResponse.body().contains("no teams") || startResponse.body().contains("no players"));
	}

	@Test
	void startBattleWithOnlyOnePlayer() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1", "team-player1");

		HttpResponse<String> startResponse = api.start(battleId);
		assertTrue(startResponse.statusCode() >= 400 && startResponse.statusCode() < 500);
		assertTrue(startResponse.body().contains("Cannot start battle") || startResponse.body().contains("need") || startResponse.body().contains("teams"));
	}

	@Test
	void startAlreadyStartedBattle() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");

		HttpResponse<String> firstStart = api.start(battleId);
		assertEquals(200, firstStart.statusCode(), "First start should succeed");

		HttpResponse<String> secondStartResponse = api.start(battleId);
		assertTrue(secondStartResponse.statusCode() >= 400 && secondStartResponse.statusCode() < 500,
				"Second start should fail");
		assertTrue(secondStartResponse.body().contains("Battle already started") || secondStartResponse.body().contains("already"));
	}

	@Test
	void verifyBattleStateAfterStart() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");

		api.start(battleId);

		String battleState = api.get(battleId).body();
		assertTrue(battleState.contains("player1"));
		assertTrue(battleState.contains("player2"));
		assertTrue(battleState.contains("turnNumber") || battleState.contains("\"turnContext\""));
	}
}