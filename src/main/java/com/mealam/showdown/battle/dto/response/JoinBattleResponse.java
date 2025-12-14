package com.mealam.showdown.battle.dto.response;

import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.context.SpectatorBattleContext;
import com.mealam.showdown.battle.data.BattleId;

public sealed interface JoinBattleResponse permits JoinBattleResponse.Player, JoinBattleResponse.Spectator {

	record Player(BattleId battleId, PlayerBattleContext playerBattleContext) implements JoinBattleResponse {
	}

	record Spectator(BattleId battleId, SpectatorBattleContext spectatorBattleContext) implements JoinBattleResponse {
	}
}