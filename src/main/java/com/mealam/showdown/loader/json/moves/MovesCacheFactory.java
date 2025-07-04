package com.mealam.showdown.loader.json.moves;

import com.mealam.showdown.loader.cache.moves.EffectCache;
import com.mealam.showdown.loader.cache.moves.EffectTargetCache;
import com.mealam.showdown.loader.cache.moves.MovesCache;
import com.mealam.showdown.loader.json.CacheFactory;
import com.mealam.showdown.loader.json.deserialize.moves.Effect;
import com.mealam.showdown.loader.json.deserialize.moves.EffectTarget;
import com.mealam.showdown.loader.json.deserialize.moves.Moves;
import com.mealam.showdown.moves.Category;
import com.mealam.showdown.moves.Status;
import com.mealam.showdown.moves.Type;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MovesCacheFactory implements CacheFactory<MovesCache, Moves> {

	public static final MovesCacheFactory INSTANCE = new MovesCacheFactory();

	private MovesCacheFactory() {}

	@Override
	public MovesCache construct(Moves pSource) {
		return constructMoves(pSource);
	}

	public MovesCache constructMoves(Moves pMoves) {
		Type typeEnum = parseType(pMoves.type());
		Category categoryEnum = parseCategory(pMoves.category());

		Effect effect = pMoves.effect();
		EffectCache effectCache = buildEffectCache(effect);

		return new MovesCache(
				pMoves.id(),
				pMoves.name(),
				pMoves.description(),
				typeEnum,
				categoryEnum,
				pMoves.power(),
				pMoves.accuracy(),
				pMoves.pp(),
				effectCache);
	}

	@Nullable
	private EffectCache buildEffectCache(@Nullable Effect pEffect) {
		if (pEffect == null) return null;
		return new EffectCache(
				buildEffectTargetCacheList(pEffect.self()),
				buildEffectTargetCacheList(pEffect.effect()));
	}

	@Nullable
	private List<EffectTargetCache> buildEffectTargetCacheList(@Nullable List<EffectTarget> pTargets) {
		if (pTargets == null) return null;
		return pTargets.stream()
				.map(this::buildEffectTargetCache)
				.toList();
	}

	@NotNull
	private EffectTargetCache buildEffectTargetCache(EffectTarget pTarget) {
		return new EffectTargetCache(
				parseStatus(pTarget.status()),
				pTarget.type(),
				pTarget.value(),
				pTarget.chance());
	}

	@NotNull
	private Type parseType(@NotNull String pType) {
		try {
			return Type.valueOf(pType.toUpperCase());
		} catch (Exception pException) {
			return Type.GENERAL;
		}
	}

	@NotNull
	private Category parseCategory(@NotNull String pCategory) {
		try {
			return Category.valueOf(pCategory.toUpperCase());
		} catch (Exception pException) {
			return Category.PHYSICAL;
		}
	}

	@Nullable
	private Status parseStatus(@Nullable String pStatus) {
		if (pStatus == null) return null;
		try {
			return Status.valueOf(pStatus.toUpperCase());
		} catch (Exception pException) {
			return Status.HARM;
		}
	}
}
