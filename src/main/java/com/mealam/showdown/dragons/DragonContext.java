package com.mealam.showdown.dragons;

import com.mealam.showdown.loader.cache.dragons.DragonsCache;
import com.mealam.showdown.moves.MoveContext;
import com.mealam.showdown.moves.Status;
import com.mealam.showdown.players.PlayerContext;

import java.util.List;

public record DragonContext(
		DragonsCache baseData,
		String dragonId,
		String name,
		int level,
		List<MoveContext> moves,
		int currentHp,
		int maxHp,
		PlayerContext owner,
		Status status
) {
}