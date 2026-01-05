package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record DragonBiology(
		List<String> diet,
		String temperament,
		List<String> fireType,
		List<String> features,
		List<String> colors
) {
	public static JsonDeserializer<DragonBiology> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonBiology> {
		@Override
		protected DragonBiology deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonBiology(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "diet")),
					GsonHelper.getAsString(pObj, "temperament"),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "fireType")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "features")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "colors"))
			);
		}

		@Override
		protected String targetTypeName() {
			return DragonBiology.class.getSimpleName();
		}
	}
}
