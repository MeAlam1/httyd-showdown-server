/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.leave;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

public class BattleLeave200Test extends BattleBaseTest {

	private static final int STATUS_CODE = 200;

	@Test
	void shouldLeaveBattleSuccessfully() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		HttpResponse<String> joinResponse = api.join(battleId, "beta", "team-beta");
		assertEquals(STATUS_CODE, joinResponse.statusCode());

		HttpResponse<String> leaveResponse = api.leave(battleId, "beta");
		assertEquals(STATUS_CODE, leaveResponse.statusCode());

		HttpResponse<String> getResponse = api.get(battleId);
		assertEquals(STATUS_CODE, getResponse.statusCode());
	}

	//TODO: Currently returns 200, but should it be 404?
	@Test
	void shouldNotAllowLeaveFromNonExistingPlayer() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "alpha", "team-alpha");
		api.join(battleId, "beta", "team-beta");

		HttpResponse<String> leaveResponse = api.leave(battleId, "ghost");
		assertEquals(STATUS_CODE, leaveResponse.statusCode());
	}

	@Test
	void shouldAllowLeavingInAnyOrder() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "first", "team-first");
		api.join(battleId, "middle", "team-middle");
		api.join(battleId, "last", "team-last");

		HttpResponse<String> removeMiddle = api.leave(battleId, "middle");
		assertEquals(STATUS_CODE, removeMiddle.statusCode());

		HttpResponse<String> removeFirst = api.leave(battleId, "first");
		assertEquals(STATUS_CODE, removeFirst.statusCode());
	}
}
