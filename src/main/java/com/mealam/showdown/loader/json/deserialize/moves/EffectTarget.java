package com.mealam.showdown.loader.json.deserialize.moves;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mealam.showdown.moves.Status;
import com.mealam.showdown.utils.json.GsonHelper;

public record EffectTarget(
		Status status,
		String type,
		Integer value,
		Integer chance

) {

	public static JsonDeserializer<EffectTarget> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Status status = GsonHelper.getAsObject(obj, "status", context, Status.class);
			String type = GsonHelper.getAsString(obj, "type");
			Integer value = GsonHelper.getAsInt(obj, "value");
			Integer chance = GsonHelper.getAsInt(obj, "chance");

			return new EffectTarget(
					status,
					type,
					value,
					chance
			);
		};
	}
}
