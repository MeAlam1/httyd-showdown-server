/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.lifecycle.finish;

import static org.junit.jupiter.api.Assertions.*;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class BattleFinish200Test extends BattleBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldFinishBattleWithWinner() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);

		HttpResponse<String> finishResponse = api.finish(battleId, "player1");
		assertEquals(STATUS_CODE, finishResponse.statusCode());
	}

	@Test
	void shouldFinishBattleWithoutWinner() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);

		HttpResponse<String> finishResponse = api.finish(battleId, null);
		assertEquals(STATUS_CODE, finishResponse.statusCode());

		String battleState = api.get(battleId).body();
		assertNotNull(battleState);
	}

	@Test
	void shouldFinishBattleBeforeStart() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");

		HttpResponse<String> finishResponse = api.finish(battleId, "player1");
		assertEquals(STATUS_CODE, finishResponse.statusCode());
	}

	@Test
	void shouldFinishBattleMultipleTimes() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "player1", "team-player1");
		api.join(battleId, "player2", "team-player2");
		api.start(battleId);

		// TODO: Actually Shouldnt, but will be added in the future

		api.finish(battleId, "player1");
		HttpResponse<String> secondFinish = api.finish(battleId, "player2");
		assertEquals(STATUS_CODE, secondFinish.statusCode());
	}

	@Test
	void shouldAllowAnyUserToWin() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p 2 🚀", "team-p2");
		api.start(battleId);

		HttpResponse<String> res = api.finish(battleId, "p 2 🚀");
		assertEquals(STATUS_CODE, res.statusCode());
	}
}
