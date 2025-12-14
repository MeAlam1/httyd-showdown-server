package com.mealam.showdown.battle.party;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

class PartyJoinLeaveTest extends PartyBaseTest {

	@Test
	void verifyPartyAfterJoin() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		joinBattle(client, battleId, "beta");

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("beta"));
	}

	@Test
	void verifyPartyAfterLeave() throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String battleId = createBattleAndGetId(client);
		assertFalse(battleId.isEmpty());

		joinBattle(client, battleId, "beta");

		String battleState = getBattleState(client, battleId);
		assertTrue(battleState.contains("beta"));

		leaveBattle(client, battleId, "beta");

		String verifyLeaveState = getBattleState(client, battleId);
		assertFalse(verifyLeaveState.contains("beta"));
	}
}