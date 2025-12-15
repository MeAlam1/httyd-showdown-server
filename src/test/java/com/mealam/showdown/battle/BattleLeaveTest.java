package com.mealam.showdown.battle;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	void leaveNonParticipantDoesNotBreakState() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "alpha", "team-alpha");
		api.join(battleId, "beta", "team-beta");

		HttpResponse<String> leaveResponse = api.leave(battleId, "ghost");
		assertTrue(leaveResponse.statusCode() == 200 || leaveResponse.statusCode() == 400 || leaveResponse.statusCode() == 404);

		String state = api.get(battleId).body();
		assertTrue(state.contains("alpha"));
		assertTrue(state.contains("beta"));
	}

	@Test
	void leavePlayersOutOfOrderMaintainsState() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "first", "team-first");
		api.join(battleId, "middle", "team-middle");
		api.join(battleId, "last", "team-last");

		HttpResponse<String> removeMiddle = api.leave(battleId, "middle");
		assertEquals(200, removeMiddle.statusCode());

		HttpResponse<String> removeFirst = api.leave(battleId, "first");
		assertEquals(200, removeFirst.statusCode());

		String state = api.get(battleId).body();
		assertFalse(state.contains("middle"));
		assertFalse(state.contains("first"));
		assertTrue(state.contains("last"), "Remaining players should stay in battle state");
	}
}