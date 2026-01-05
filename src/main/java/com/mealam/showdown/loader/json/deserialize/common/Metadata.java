package com.mealam.showdown.loader.json.deserialize.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record Metadata(
		String introducedIn,
		String lastUpdated
) {
	public static JsonDeserializer<Metadata> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Metadata> {
		@Override
		protected Metadata deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Metadata(
					GsonHelper.getAsString(pObj, "introducedIn"),
					GsonHelper.getAsString(pObj, "lastUpdated")
			);
		}

		@Override
		protected String targetTypeName() {
			return Metadata.class.getSimpleName();
		}
	}
}
