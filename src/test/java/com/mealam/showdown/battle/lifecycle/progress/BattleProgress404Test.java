package com.mealam.showdown.battle.lifecycle.progress;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleProgress404Test extends BattleBaseTest {

	private static final int STATUS_CODE = 404;

	@Test
	void shouldNotAdvanceTurnInNonExistentBattle() throws Exception {
		String turnData = loadResource("turnAction");
		HttpResponse<String> turnResponse = api.turn("nonexistent-id", "player1", turnData);
		assertEquals(STATUS_CODE, turnResponse.statusCode(), "Battle could not be found");
	}
}
