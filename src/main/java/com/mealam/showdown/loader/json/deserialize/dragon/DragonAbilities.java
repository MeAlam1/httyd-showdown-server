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
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record DragonAbilities() {

	public static JsonDeserializer<DragonAbilities> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonAbilities> {

		@Override
		protected DragonAbilities deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonAbilities();
		}

		@Override
		protected String targetTypeName() {
			return DragonAbilities.class.getSimpleName();
		}
	}
}
