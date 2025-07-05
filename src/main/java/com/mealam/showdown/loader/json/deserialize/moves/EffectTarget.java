package com.mealam.showdown.loader.json.deserialize.moves;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mealam.showdown.utils.json.JsonObjectExtensionsKt;

public record EffectTarget(
		String status,
		String type,
		Integer value,
		Integer chance) {

	public static JsonDeserializer<EffectTarget> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String status = JsonObjectExtensionsKt.getAsString(obj, "status");
			String type = JsonObjectExtensionsKt.getAsString(obj, "type");
			Integer value = JsonObjectExtensionsKt.getAsInt(obj, "value");
			Integer chance = JsonObjectExtensionsKt.getAsInt(obj, "chance");

			return new EffectTarget(
					status,
					type,
					value,
					chance);
		};
	}
}
