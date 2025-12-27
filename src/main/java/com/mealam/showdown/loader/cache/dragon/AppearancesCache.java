package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.Appearances;

import java.util.List;

public record AppearancesCache(
		List<String> movies,
		List<String> series,
		List<String> games
) {
	public static AppearancesCache construct(Appearances pAppearance) {
		return new AppearancesCache(
				pAppearance.movies(),
				pAppearance.series(),
				pAppearance.games()
		);
	}
}