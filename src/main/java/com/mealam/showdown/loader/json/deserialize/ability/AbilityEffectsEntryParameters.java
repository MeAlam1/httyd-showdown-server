package com.mealam.showdown.loader.json.deserialize.ability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record AbilityEffectsEntryParameters(
		String status,
		int durationSeconds,
		int radiusMeters,
		float chance,
		int maxStacks,
		String stackBehavior,
		String resistType
) {
	public static JsonDeserializer<AbilityEffectsEntryParameters> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityEffectsEntryParameters> {
		@Override
		protected AbilityEffectsEntryParameters deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityEffectsEntryParameters(
					GsonHelper.getAsString(pObj, "status"),
					GsonHelper.getAsInt(pObj, "durationSeconds"),
					GsonHelper.getAsInt(pObj, "radiusMeters"),
					GsonHelper.getAsFloat(pObj, "chance"),
					GsonHelper.getAsInt(pObj, "maxStacks"),
					GsonHelper.getAsString(pObj, "stackBehavior"),
					GsonHelper.getAsString(pObj, "resistType")
			);
		}

		@Override
		protected String targetTypeName() {
			return AbilityEffectsEntryParameters.class.getSimpleName();
		}
	}
}
