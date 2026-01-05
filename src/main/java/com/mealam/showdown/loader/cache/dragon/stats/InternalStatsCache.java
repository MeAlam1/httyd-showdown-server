package com.mealam.showdown.loader.cache.dragon.stats;

import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonInternalStats;

import java.util.List;

public record InternalStatsCache(
		List<String> notes,
		float attack,
		float speed,
		float defense,
		float firepower,
		float stealth,
		float stamina
) {
	public static InternalStatsCache construct(DragonInternalStats pSource) {
		return new InternalStatsCache(
				pSource.notes(),
				pSource.attack(),
				pSource.speed(),
				pSource.defense(),
				pSource.firepower(),
				pSource.stealth(),
				pSource.stamina()
		);
	}
}