package com.mealam.showdown.moves;

import com.mealam.showdown.battle.BattleContext;
import com.mealam.showdown.dragons.DragonContext;
import com.mealam.showdown.loader.cache.moves.MovesCache;

public record MoveContext(
		MovesCache baseData,
		DragonContext user,
		DragonContext target,
		BattleContext battle
) {
}