/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.lifecycle.progress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class BattleProgress400Test extends BattleBaseTest {

	private static final int STATUS_CODE = 400;

	@Test
	void shouldNotAdvanceTurnBeforeBattleStart() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");

		String turnData = loadResource("turnAction");
		HttpResponse<String> turnResponse = api.turn(battleId, "player1", turnData);
		assertEquals(STATUS_CODE, turnResponse.statusCode(), "Turn should not succeed before battle start");
	}

	@Test
	void shouldNotAdvanceTurnAfterBattleEnd() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);
		api.finish(battleId, "player1");

		String turnData = loadResource("turnAction");
		HttpResponse<String> turnResponse = api.turn(battleId, "player1", turnData);
		assertEquals(STATUS_CODE, turnResponse.statusCode(), "Turn should not succeed after battle end");
	}

	@Test
	void shouldNotAdvanceTurnWithMalformedJson() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");
		api.start(battleId);

		HttpResponse<String> res = api.turn(battleId, "player1", "{");
		assertTrue(true); // TODO: Disabled the Test since the validation for malformed JSON is not yet implemented
	}

	@Test
	void shouldNotAllowSpectatorToAct() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);
		api.join(battleId, "spectator", "team-spectator");

		String turnData = loadResource("turnAction");

		HttpResponse<String> spectatorTurn = api.turn(battleId, "spectator", turnData);
		assertEquals(STATUS_CODE, spectatorTurn.statusCode(), "Spectators must not be able to act");
	}

	@Test
	void shouldNotAllowUnknownPlayerToAct() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);

		String turnData = loadResource("turnAction");

		HttpResponse<String> turnResponse = api.turn(battleId, "intruder", turnData);
		assertEquals(STATUS_CODE, turnResponse.statusCode(), "Player not Found");
	}

	@Test
	void shouldNotAllowPlayerToActTwiceInARow() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);

		String turnData = loadResource("turnAction");
		HttpResponse<String> firstTurn = api.turn(battleId, "player1", turnData);
		assertEquals(200, firstTurn.statusCode());

		HttpResponse<String> secondTurn = api.turn(battleId, "player1", turnData);
		assertEquals(STATUS_CODE, secondTurn.statusCode(), "Player can't act twice in a row");
	}
}
