package com.mealam.showdown.loader.cache.dragon;

import com.mealam.showdown.loader.json.deserialize.dragon.Biology;

import java.util.List;

public record BiologyCache(
		List<String> diet,
		String temperament,
		List<String> fireType,
		List<String> features,
		List<String> colors
) {
	public static BiologyCache construct(Biology pBiology) {
		return new BiologyCache(
				pBiology.diet(),
				pBiology.temperament(),
				pBiology.fireType(),
				pBiology.features(),
				pBiology.colors()
		);
	}
}