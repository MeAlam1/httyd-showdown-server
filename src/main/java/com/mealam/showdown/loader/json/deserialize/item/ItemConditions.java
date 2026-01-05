package com.mealam.showdown.loader.json.deserialize.item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record ItemConditions(
		List<String> requiresTags,
		ItemConditionsBlockedIf blockedIf,
		int minLevel
) {
	public static JsonDeserializer<ItemConditions> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<ItemConditions> {
		@Override
		protected ItemConditions deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new ItemConditions(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "requiresTags")),
					GsonHelper.getAsObject(pObj, "blockedIf", pContext, ItemConditionsBlockedIf.class),
					GsonHelper.getAsInt(pObj, "minLevel")
			);
		}

		@Override
		protected String targetTypeName() {
			return ItemConditions.class.getSimpleName();
		}
	}
}
