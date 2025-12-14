package com.mealam.showdown.battle.dto.response;

import com.mealam.showdown.battle.context.BattleContext;

public record TurnAdvanceResponse(
		String battleId,
		int turnNumber
) {
	public static TurnAdvanceResponse from(BattleContext ctx) {
		return new TurnAdvanceResponse(
				ctx.battleId().toString(),
				ctx.turnContext() != null ? ctx.turnContext().turnNumber() : -1
		);
	}
}