package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record DragonStats(
		DragonInternalStats internal,
		DragonExternalStats external
) {
	public static JsonDeserializer<DragonStats> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonStats> {
		@Override
		protected DragonStats deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonStats(
					GsonHelper.getAsObject(pObj, "internal", pContext, DragonInternalStats.class),
					GsonHelper.getAsObject(pObj, "external", pContext, DragonExternalStats.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return DragonStats.class.getSimpleName();
		}
	}
}
