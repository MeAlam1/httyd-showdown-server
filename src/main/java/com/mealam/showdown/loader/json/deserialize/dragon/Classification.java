package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Classification(
		@SerializedName("class") List<String> clazz,
		@Nullable String former,
		String size,
		String habitat
) {
	public static JsonDeserializer<Classification> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Classification> {
		@Override
		protected Classification deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Classification(
					JsonUtils.jsonArrayToStringList(JsonUtils.getOptionalJsonArray(pObj, "class")),
					JsonUtils.getOptionalString(pObj, "former"),
					GsonHelper.getAsString(pObj, "size"),
					GsonHelper.getAsString(pObj, "habitat")
			);
		}

		@Override
		protected String targetTypeName() {
			return Classification.class.getSimpleName();
		}
	}
}

