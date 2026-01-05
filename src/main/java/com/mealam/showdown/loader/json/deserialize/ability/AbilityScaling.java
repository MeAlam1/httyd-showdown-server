package com.mealam.showdown.loader.json.deserialize.ability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record AbilityScaling(
		String withStat,
		float multiplier,
		float perLevelMultiplier,
		int maxMultiplier,
		int capAtLevel
) {
	public static JsonDeserializer<AbilityScaling> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityScaling> {
		@Override
		protected AbilityScaling deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityScaling(
					GsonHelper.getAsString(pObj, "withStat"),
					GsonHelper.getAsFloat(pObj, "multiplier"),
					GsonHelper.getAsFloat(pObj, "perLevelMultiplier"),
					GsonHelper.getAsInt(pObj, "maxMultiplier"),
					GsonHelper.getAsInt(pObj, "capAtLevel")
			);
		}

		@Override
		protected String targetTypeName() {
			return AbilityScaling.class.getSimpleName();
		}
	}
}
