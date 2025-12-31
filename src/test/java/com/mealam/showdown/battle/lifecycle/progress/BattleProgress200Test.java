package com.mealam.showdown.battle.lifecycle.progress;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleProgress200Test extends BattleBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldAdvanceTurn() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");

		HttpResponse<String> startResp = api.start(battleId);
		assertEquals(STATUS_CODE, startResp.statusCode(), "Start should succeed");

		String turnData = loadResource("turnAction");
		HttpResponse<String> turnResponse = api.turn(battleId, "player1", turnData);
		assertEquals(STATUS_CODE, turnResponse.statusCode(), "Turn should succeed for player1");
	}

	@Test
	void shouldAdvanceMultipleTurns() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		HttpResponse<String> startResp = api.start(battleId);
		assertEquals(STATUS_CODE, startResp.statusCode(), "Start should succeed");

		String turnData = loadResource("turnAction");

		HttpResponse<String> r1 = api.turn(battleId, "player1", turnData);
		assertEquals(STATUS_CODE, r1.statusCode(), "First turn by player1 should succeed");

		HttpResponse<String> r1Again = api.turn(battleId, "player1", turnData);
		assertEquals(400, r1Again.statusCode(), "player1 cannot act twice in same turn");

		HttpResponse<String> r2 = api.turn(battleId, "player2", turnData);
		assertEquals(STATUS_CODE, r2.statusCode(), "Turn by player2 should succeed");

		HttpResponse<String> r2Again = api.turn(battleId, "player2", turnData);
		assertEquals(400, r2Again.statusCode(), "player2 cannot act twice");

		HttpResponse<String> r3 = api.turn(battleId, "player1", turnData);
		assertEquals(STATUS_CODE, r3.statusCode(), "Next turn by player1 should succeed");
	}
}
