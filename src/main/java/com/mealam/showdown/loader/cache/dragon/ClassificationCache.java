package com.mealam.showdown.loader.cache.dragon;

import com.google.gson.annotations.SerializedName;
import com.mealam.showdown.loader.json.deserialize.dragon.DragonClassification;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ClassificationCache(
		@SerializedName("class") List<String> clazz,
		@Nullable String former,
		String size,
		String habitat
) {
	public static ClassificationCache construct(DragonClassification pClassification) {
		return new ClassificationCache(
				pClassification.clazz(),
				pClassification.former(),
				pClassification.size(),
				pClassification.habitat()
		);
	}
}