/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record DragonClassification(
		@SerializedName("class") List<String> clazz,
		@Nullable String former,
		String size,
		String habitat) {

	public static JsonDeserializer<DragonClassification> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonClassification> {

		@Override
		protected DragonClassification deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonClassification(
					JsonUtils.jsonArrayToStringList(JsonUtils.getOptionalJsonArray(pObj, "class")),
					JsonUtils.getOptionalString(pObj, "former"),
					GsonHelper.getAsString(pObj, "size"),
					GsonHelper.getAsString(pObj, "habitat"));
		}

		@Override
		protected String targetTypeName() {
			return DragonClassification.class.getSimpleName();
		}
	}
}
