package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;

import java.util.List;

public record Biology(
		List<String> diet,
		String temperament,
		List<String> fireType,
		List<String> features,
		List<String> colors
) {
	public static JsonDeserializer<Biology> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Biology> {
		@Override
		protected Biology deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Biology(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "diet")),
					GsonHelper.getAsString(pObj, "temperament"),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "fireType")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "features")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "colors"))
			);
		}

		@Override
		protected String targetTypeName() {
			return Biology.class.getSimpleName();
		}
	}
}
