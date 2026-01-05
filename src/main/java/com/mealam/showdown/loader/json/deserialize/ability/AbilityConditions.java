/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.ability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record AbilityConditions(
		List<String> blockedIn,
		List<String> requiresTagOnCaster,
		List<AbilityConditionsRequiresItemsEntry> requiresItems) {

	public static JsonDeserializer<AbilityConditions> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityConditions> {

		@Override
		protected AbilityConditions deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityConditions(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "blockedIn")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "requiresTagOnCaster")),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "requiresItems"), pContext, AbilityConditionsRequiresItemsEntry.class));
		}

		@Override
		protected String targetTypeName() {
			return AbilityConditions.class.getSimpleName();
		}
	}
}
