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
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record ItemEffectsEntry(
		String effectId,
		ItemEffectsEntryParameters parameters) {

	public static JsonDeserializer<ItemEffectsEntry> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<ItemEffectsEntry> {

		@Override
		protected ItemEffectsEntry deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new ItemEffectsEntry(
					GsonHelper.getAsString(pObj, "effectId"),
					GsonHelper.getAsObject(pObj, "parameters", pContext, ItemEffectsEntryParameters.class));
		}

		@Override
		protected String targetTypeName() {
			return ItemEffectsEntry.class.getSimpleName();
		}
	}
}
