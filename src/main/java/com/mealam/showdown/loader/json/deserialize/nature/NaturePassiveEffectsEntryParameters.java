package com.mealam.showdown.loader.json.deserialize.nature;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record NaturePassiveEffectsEntryParameters(
		float multiplier,
		String appliesTo
) {
	public static JsonDeserializer<NaturePassiveEffectsEntryParameters> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<NaturePassiveEffectsEntryParameters> {
		@Override
		protected NaturePassiveEffectsEntryParameters deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new NaturePassiveEffectsEntryParameters(
					GsonHelper.getAsFloat(pObj, "multiplier"),
					GsonHelper.getAsString(pObj, "appliesTo")
			);
		}

		@Override
		protected String targetTypeName() {
			return NaturePassiveEffectsEntryParameters.class.getSimpleName();
		}
	}
}
