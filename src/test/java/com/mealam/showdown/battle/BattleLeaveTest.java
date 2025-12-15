package com.mealam.showdown.battle;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BattleLeaveTest extends BattleBaseTest {

	@Test
	void leaveBattle() throws Exception {
		String battleId = api.createBattleAndGetId();
		assertFalse(battleId.isEmpty());

		HttpResponse<String> joinResponse = api.join(battleId, "beta", "team-beta");
		assertEquals(200, joinResponse.statusCode());

		HttpResponse<String> leaveResponse = api.leave(battleId, "beta");
		assertEquals(200, leaveResponse.statusCode());

		HttpResponse<String> getResponse = api.get(battleId);
		assertEquals(200, getResponse.statusCode());
		assertFalse(getResponse.body().contains("beta"));
	}
}