package com.mealam.showdown.battle.lifecycle.finish;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleFinish404Test extends BattleBaseTest {

	private static final int STATUS_CODE = 404;

	@Test
	void shouldNotFinishNonExistentBattle() throws Exception {
		HttpResponse<String> finishResponse = api.finish("nonexistent-id", "player1");
		assertEquals(STATUS_CODE, finishResponse.statusCode());
		assertTrue(finishResponse.body().contains("Battle not found"));
	}
}
