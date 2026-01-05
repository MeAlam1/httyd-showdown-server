/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.lifecycle.finish;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

public class BattleFinish404Test extends BattleBaseTest {

	private static final int STATUS_CODE = 404;

	@Test
	void shouldNotFinishNonExistentBattle() throws Exception {
		HttpResponse<String> finishResponse = api.finish("nonexistent-id", "player1");
		assertEquals(STATUS_CODE, finishResponse.statusCode());
		assertTrue(finishResponse.body().contains("Battle not found"));
	}
}
