package com.mealam.showdown.battle;

import com.mealam.showdown.players.PlayerContext;

import java.util.List;

public record PlayerBattleContext(
		PlayerContext player,
		List<DragonBattleContext> party,
		boolean isTurn
) {
}