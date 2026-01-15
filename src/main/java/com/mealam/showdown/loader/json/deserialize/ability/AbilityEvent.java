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
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record AbilityEvent() {

	public static JsonDeserializer<AbilityEvent> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityEvent> {

		@Override
		protected AbilityEvent deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityEvent();
		}

		@Override
		protected String targetTypeName() {
			return AbilityEvent.class.getSimpleName();
		}
	}
}
