package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record ExternalStats(
		List<String> notes,
		ExternalValues values
) {
	public static JsonDeserializer<ExternalStats> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<ExternalStats> {
		@Override
		protected ExternalStats deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new ExternalStats(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "notes")),
					GsonHelper.getAsObject(pObj, "values", pContext, ExternalValues.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return ExternalStats.class.getSimpleName();
		}
	}
}
