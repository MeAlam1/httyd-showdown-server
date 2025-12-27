package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record ExternalValues(
		Float attack,
		Float speed,
		Float armor,
		Float firepower,
		Float shotLimit,
		Float venom,
		Float jawStrength,
		Float stealth
) {
	public static JsonDeserializer<ExternalValues> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<ExternalValues> {
		@Override
		protected ExternalValues deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new ExternalValues(
					JsonUtils.getOptionalFloat(pObj, "attack"),
					JsonUtils.getOptionalFloat(pObj, "speed"),
					JsonUtils.getOptionalFloat(pObj, "armor"),
					JsonUtils.getOptionalFloat(pObj, "firepower"),
					JsonUtils.getOptionalFloat(pObj, "shotLimit"),
					JsonUtils.getOptionalFloat(pObj, "venom"),
					JsonUtils.getOptionalFloat(pObj, "jawStrength"),
					JsonUtils.getOptionalFloat(pObj, "stealth")
			);
		}

		@Override
		protected String targetTypeName() {
			return ExternalValues.class.getSimpleName();
		}
	}
}
