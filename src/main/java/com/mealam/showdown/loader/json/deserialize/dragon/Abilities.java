package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record Abilities() {
	public static JsonDeserializer<Abilities> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Abilities> {
		@Override
		protected Abilities deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Abilities();
		}

		@Override
		protected String targetTypeName() {
			return Abilities.class.getSimpleName();
		}
	}
}