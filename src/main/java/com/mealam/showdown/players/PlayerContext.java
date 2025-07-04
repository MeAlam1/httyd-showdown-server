package com.mealam.showdown.players;

import com.mealam.showdown.battle.BattleContext;
import com.mealam.showdown.dragons.DragonContext;

import java.util.List;

public record PlayerContext(
		String playerId,
		String username,
		List<PlayerContext> friends,
		List<DragonContext> party,
		BattleContext currentBattle
) {
}