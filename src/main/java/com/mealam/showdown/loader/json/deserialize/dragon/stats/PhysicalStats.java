package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.common.Measurement;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record PhysicalStats(
		Measurement length,
		Measurement weight,
		Measurement wingspan
) {
	public static JsonDeserializer<PhysicalStats> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<PhysicalStats> {
		@Override
		protected PhysicalStats deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new PhysicalStats(
					GsonHelper.getAsObject(pObj, "length", pContext, Measurement.class),
					GsonHelper.getAsObject(pObj, "weight", pContext, Measurement.class),
					GsonHelper.getAsObject(pObj, "wingspan", pContext, Measurement.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return PhysicalStats.class.getSimpleName();
		}
	}
}
