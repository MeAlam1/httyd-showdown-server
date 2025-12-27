package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record Appearances(
		List<String> movies,
		List<String> series,
		List<String> games
) {
	public static JsonDeserializer<Appearances> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Appearances> {
		@Override
		protected Appearances deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Appearances(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "movies")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "series")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "games"))
			);
		}

		@Override
		protected String targetTypeName() {
			return Appearances.class.getSimpleName();
		}
	}
}
