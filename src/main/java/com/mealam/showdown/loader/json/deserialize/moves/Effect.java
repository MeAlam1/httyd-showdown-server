package com.mealam.showdown.loader.json.deserialize.moves;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mealam.showdown.utils.json.JsonUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Effect(
		@Nullable List<EffectTarget> self,
		@Nullable List<EffectTarget> effect
) {

	public static JsonDeserializer<Effect> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<EffectTarget> self = JsonUtils.jsonArrayToObjectList(JsonUtils.getOptionalJsonArray(obj, "self"), context, EffectTarget.class);
			List<EffectTarget> effect = JsonUtils.jsonArrayToObjectList(JsonUtils.getOptionalJsonArray(obj, "enemy"), context, EffectTarget.class);

			return new Effect(
					self,
					effect
			);
		};
	}
}
