package com.mealam.showdown.loader.json.moves;

import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.moves.Moves;

public class MovesCacheFactory implements CacheFactory<MovesCache, Moves> {

	public static final MovesCacheFactory INSTANCE = new MovesCacheFactory();

	private MovesCacheFactory() {
	}

	@Override
	public MovesCache construct(Moves pSource) {
		return constructMoves(pSource);
	}

	public MovesCache constructMoves(Moves pMoves) {
		return null;
	}
}