package com.mealam.showdown.battle.dto.response;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;

import java.util.List;

public record BattleSummaryResponse(
		String battleId,
		List<String> playerIds,
		List<String> spectatorIds,
		int turnNumber,
		String phase,
		String winnerPlayerId
) {
	public static BattleSummaryResponse from(BattleContext ctx) {
		return new BattleSummaryResponse(
				ctx.battleId().toString(),
				ctx.playerIds() != null ? ctx.playerIds().stream().map(Object::toString).toList() : List.of(),
				ctx.spectatorIds() != null ? ctx.spectatorIds().stream().map(Object::toString).toList() : List.of(),
				ctx.turnContext() != null ? ctx.turnContext().turnNumber() : -1,
				ctx.phase().toString(),
				ctx.winnerPlayerId() != null ? ctx.winnerPlayerId().toString() : null
		);
	}

	public static BattleSummaryResponse created(BattleId id, BattleContext ctx) {
		return from(ctx);
	}
}