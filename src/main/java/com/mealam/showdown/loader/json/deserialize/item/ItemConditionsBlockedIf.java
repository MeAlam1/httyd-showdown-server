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

public record ItemConditionsBlockedIf(
		String dragonSize) {

	public static JsonDeserializer<ItemConditionsBlockedIf> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<ItemConditionsBlockedIf> {

		@Override
		protected ItemConditionsBlockedIf deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new ItemConditionsBlockedIf(
					GsonHelper.getAsString(pObj, "dragonSize"));
		}

		@Override
		protected String targetTypeName() {
			return ItemConditionsBlockedIf.class.getSimpleName();
		}
	}
}
