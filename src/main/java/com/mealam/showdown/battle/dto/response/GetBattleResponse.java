package com.mealam.showdown.battle.dto.response;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.context.PlayerBattleContext;

public record GetBattleResponse(
		BattleContext battleContext,
		PlayerBattleContext playerBattleContext
) {
}