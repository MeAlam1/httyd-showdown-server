package com.mealam.showdown.battle.lifecycle.start;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class BattleStart400Test extends BattleBaseTest {

	private static final int STATUS_CODE = 400;

	@Test
	void shouldNotStartBattleWithInsufficientPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1", "team-player1");

		HttpResponse<String> startResponse = api.start(battleId);
		assertEquals(STATUS_CODE, startResponse.statusCode(), "Battle cannot start with insufficient players");
	}

	@Test
	void shouldNotStartBattleAfterAlreadyStarting() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");

		HttpResponse<String> firstStart = api.start(battleId);
		assertEquals(200, firstStart.statusCode(), "First start should succeed");

		HttpResponse<String> secondStartResponse = api.start(battleId);
		assertEquals(STATUS_CODE, secondStartResponse.statusCode(), "Battle cannot start again");
	}
}