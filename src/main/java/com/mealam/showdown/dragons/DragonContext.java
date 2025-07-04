package com.mealam.showdown.dragons;

import com.mealam.showdown.loader.cache.dragons.DragonsCache;
import com.mealam.showdown.moves.MoveContext;
import com.mealam.showdown.moves.enums.Status;

import java.util.List;

public record DragonContext(
		DragonsCache baseData,
		String dragonId,
		String name,
		int level,
		List<MoveContext> moves,
		int currentHp,
		int maxHp,
		Status status
) {
}