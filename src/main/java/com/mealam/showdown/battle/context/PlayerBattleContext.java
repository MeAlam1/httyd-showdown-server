package com.mealam.showdown.battle.context;

import com.mealam.showdown.context.PlayerContext;
import java.util.List;

public record PlayerBattleContext(
		PlayerContext player,
		List<DragonBattleContext> party,
		boolean isTurn) {}
