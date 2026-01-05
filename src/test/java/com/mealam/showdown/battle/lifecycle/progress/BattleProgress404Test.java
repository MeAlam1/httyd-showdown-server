/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.lifecycle.progress;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

public class BattleProgress404Test extends BattleBaseTest {

	private static final int STATUS_CODE = 404;

	@Test
	void shouldNotAdvanceTurnInNonExistentBattle() throws Exception {
		String turnData = loadResource("turnAction");
		HttpResponse<String> turnResponse = api.turn("nonexistent-id", "player1", turnData);
		assertEquals(STATUS_CODE, turnResponse.statusCode(), "Battle could not be found");
	}
}
