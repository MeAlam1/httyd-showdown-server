package com.mealam.showdown.loader.json.deserialize.nature;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record NaturePassiveEffectsEntry(
		String effectId,
		NaturePassiveEffectsEntryParameters parameters
) {
	public static JsonDeserializer<NaturePassiveEffectsEntry> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<NaturePassiveEffectsEntry> {
		@Override
		protected NaturePassiveEffectsEntry deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new NaturePassiveEffectsEntry(
					GsonHelper.getAsString(pObj, "effectId"),
					GsonHelper.getAsObject(pObj, "parameters", pContext, NaturePassiveEffectsEntryParameters.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return NaturePassiveEffectsEntry.class.getSimpleName();
		}
	}
}
