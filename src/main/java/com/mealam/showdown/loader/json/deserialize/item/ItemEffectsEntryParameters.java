/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record ItemEffectsEntryParameters(
		String element,
		float multiplier,
		@Nullable String durationSeconds,
		List<String> appliesTo) {

	public static JsonDeserializer<ItemEffectsEntryParameters> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<ItemEffectsEntryParameters> {

		@Override
		protected ItemEffectsEntryParameters deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new ItemEffectsEntryParameters(
					GsonHelper.getAsString(pObj, "element"),
					GsonHelper.getAsFloat(pObj, "multiplier"),
					JsonUtils.getOptionalString(pObj, "durationSeconds"),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "appliesTo")));
		}

		@Override
		protected String targetTypeName() {
			return ItemEffectsEntryParameters.class.getSimpleName();
		}
	}
}
