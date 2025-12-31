package com.mealam.showdown.battle.lifecycle.start;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BattleStart200Test extends BattleBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldStartBattleWithTwoPlayers() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		HttpResponse<String> join1 = api.join(battleId, "player1", "team-player1");
		assertEquals(STATUS_CODE, join1.statusCode(), "First join should succeed");

		HttpResponse<String> join2 = api.join(battleId, "player2", "team-player2");
		assertEquals(STATUS_CODE, join2.statusCode(), "Second join should succeed");

		HttpResponse<String> startResponse = api.start(battleId);
		assertEquals(STATUS_CODE, startResponse.statusCode(), "Start should succeed with 2 players");
	}
}
