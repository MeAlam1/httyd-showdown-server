package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record DragonAbilities() {
	public static JsonDeserializer<DragonAbilities> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonAbilities> {
		@Override
		protected DragonAbilities deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonAbilities();
		}

		@Override
		protected String targetTypeName() {
			return DragonAbilities.class.getSimpleName();
		}
	}
}