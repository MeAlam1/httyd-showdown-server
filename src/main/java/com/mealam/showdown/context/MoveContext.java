package com.mealam.showdown.context;

import com.mealam.showdown.loader.cache.moves.MovesCache;
import org.jetbrains.annotations.Nullable;

public record MoveContext(
		MovesCache baseData,
		String userDragonId,
		@Nullable String targetDragonId
) {
}