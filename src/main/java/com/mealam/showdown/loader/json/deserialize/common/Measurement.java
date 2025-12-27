package com.mealam.showdown.loader.json.deserialize.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import org.jetbrains.annotations.Nullable;

public record Measurement(
		@Nullable Float ft,
		@Nullable Float meters,
		@Nullable Float inches,
		@Nullable Float centimeters,
		@Nullable Float yards,
		@Nullable Float kilometers,
		@Nullable Float miles
) {
	public static JsonDeserializer<Measurement> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Measurement> {
		@Override
		protected Measurement deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Measurement(
					JsonUtils.getOptionalFloat(pObj, "ft"),
					JsonUtils.getOptionalFloat(pObj, "meters"),
					JsonUtils.getOptionalFloat(pObj, "inches"),
					JsonUtils.getOptionalFloat(pObj, "centimeters"),
					JsonUtils.getOptionalFloat(pObj, "yards"),
					JsonUtils.getOptionalFloat(pObj, "kilometers"),
					JsonUtils.getOptionalFloat(pObj, "miles")
			);
		}

		@Override
		protected String targetTypeName() {
			return Measurement.class.getSimpleName();
		}
	}
}