package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record DragonInternalStats(
		List<String> notes,
		float attack,
		float speed,
		float defense,
		float firepower,
		float stealth,
		float stamina
) {
	public static JsonDeserializer<DragonInternalStats> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonInternalStats> {
		@Override
		protected DragonInternalStats deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonInternalStats(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "notes")),
					GsonHelper.getAsFloat(pObj, "attack"),
					GsonHelper.getAsFloat(pObj, "speed"),
					GsonHelper.getAsFloat(pObj, "defense"),
					GsonHelper.getAsFloat(pObj, "firepower"),
					GsonHelper.getAsFloat(pObj, "stealth"),
					GsonHelper.getAsFloat(pObj, "stamina")
			);
		}

		@Override
		protected String targetTypeName() {
			return DragonInternalStats.class.getSimpleName();
		}
	}
}