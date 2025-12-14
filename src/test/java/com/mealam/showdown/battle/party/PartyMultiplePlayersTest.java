package com.mealam.showdown.battle.party;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

class PartyMultiplePlayersTest extends PartyBaseTest {

	@Test
	void verifyTwoPlayersWithRandomIds() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		String userId1 = generateRandomUserId();
		String userId2 = generateRandomUserId();

		joinBattle(client, battleId, userId1);
		joinBattle(client, battleId, userId2);

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains(userId1));
		assertTrue(battleState.contains(userId2));
	}

	@Test
	void verifyThirdPlayerBecomesSpectator() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		String userId1 = generateRandomUserId();
		String userId2 = generateRandomUserId();
		String userId3 = generateRandomUserId();

		joinBattle(client, battleId, userId1);
		joinBattle(client, battleId, userId2);
		joinBattle(client, battleId, userId3);

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains(userId1));
		assertTrue(battleState.contains(userId2));
		assertTrue(battleState.contains(userId3));
		assertTrue(battleState.contains("spectator") || battleState.contains("observers"));
	}
}