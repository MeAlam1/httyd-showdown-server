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

public record NatureGrowthCurves(
		String attack,
		String stamina,
		String critChance) {

	public static JsonDeserializer<NatureGrowthCurves> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<NatureGrowthCurves> {

		@Override
		protected NatureGrowthCurves deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new NatureGrowthCurves(
					GsonHelper.getAsString(pObj, "attack"),
					GsonHelper.getAsString(pObj, "stamina"),
					GsonHelper.getAsString(pObj, "critChance"));
		}

		@Override
		protected String targetTypeName() {
			return NatureGrowthCurves.class.getSimpleName();
		}
	}
}
