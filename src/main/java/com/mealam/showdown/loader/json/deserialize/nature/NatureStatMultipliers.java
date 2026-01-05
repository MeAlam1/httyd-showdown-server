/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.nature;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record NatureStatMultipliers(
		float attack,
		float stamina,
		float defense,
		float critChance,
		float speed) {

	public static JsonDeserializer<NatureStatMultipliers> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<NatureStatMultipliers> {

		@Override
		protected NatureStatMultipliers deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new NatureStatMultipliers(
					GsonHelper.getAsFloat(pObj, "attack"),
					GsonHelper.getAsFloat(pObj, "stamina"),
					GsonHelper.getAsFloat(pObj, "defense"),
					GsonHelper.getAsFloat(pObj, "critChance"),
					GsonHelper.getAsFloat(pObj, "speed"));
		}

		@Override
		protected String targetTypeName() {
			return NatureStatMultipliers.class.getSimpleName();
		}
	}
}
