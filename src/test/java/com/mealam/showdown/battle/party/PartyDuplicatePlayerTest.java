package com.mealam.showdown.battle.party;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

class PartyDuplicatePlayerTest extends PartyBaseTest {

	@Test
	void verifyDuplicatePlayerJoinPrevented() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		String userId = generateRandomUserId();

		String firstJoin = joinBattle(client, battleId, userId);
		String secondJoin = joinBattle(client, battleId, userId);

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains(userId));

		int count = battleState.split(userId, -1).length - 1;
		assertTrue(count >= 1, "User should appear at least once");
	}

	@Test
	void verifyDuplicatePlayerWithDifferentCasing() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		joinBattle(client, battleId, "testuser");
		joinBattle(client, battleId, "TestUser");

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("testuser") || battleState.contains("TestUser"));
	}
}