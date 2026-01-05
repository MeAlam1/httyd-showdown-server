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
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record AbilityConditionsRequiresItemsEntry(
		String itemId,
		boolean consume) {

	public static JsonDeserializer<AbilityConditionsRequiresItemsEntry> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityConditionsRequiresItemsEntry> {

		@Override
		protected AbilityConditionsRequiresItemsEntry deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityConditionsRequiresItemsEntry(
					GsonHelper.getAsString(pObj, "itemId"),
					GsonHelper.getAsBoolean(pObj, "consume"));
		}

		@Override
		protected String targetTypeName() {
			return AbilityConditionsRequiresItemsEntry.class.getSimpleName();
		}
	}
}
