package com.mealam.showdown.battle.context;

import com.mealam.showdown.move.enums.Status;
import java.util.List;

public record DragonBattleContext(
		String dragonId,
		String name,
		int level,
		int currentHp,
		int maxHp,
		Status status,
		List<MoveBattleContext> moves,
		String ownerPlayerId,
		boolean isActive) {}
