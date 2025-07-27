package com.mealam.showdown.battle.dto.response;

import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.context.SpectatorBattleContext;

public sealed interface JoinBattleResponse permits JoinBattleResponse.Player, JoinBattleResponse.Spectator {
    record Player(PlayerBattleContext playerBattleContext) implements JoinBattleResponse {}
    record Spectator(SpectatorBattleContext spectatorBattleContext) implements JoinBattleResponse {}
}