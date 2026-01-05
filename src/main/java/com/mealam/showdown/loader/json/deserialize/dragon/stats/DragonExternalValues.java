/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record DragonExternalValues(
		Float attack,
		Float speed,
		Float armor,
		Float firepower,
		Float shotLimit,
		Float venom,
		Float jawStrength,
		Float stealth) {

	public static JsonDeserializer<DragonExternalValues> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonExternalValues> {

		@Override
		protected DragonExternalValues deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonExternalValues(
					JsonUtils.getOptionalFloat(pObj, "attack"),
					JsonUtils.getOptionalFloat(pObj, "speed"),
					JsonUtils.getOptionalFloat(pObj, "armor"),
					JsonUtils.getOptionalFloat(pObj, "firepower"),
					JsonUtils.getOptionalFloat(pObj, "shotLimit"),
					JsonUtils.getOptionalFloat(pObj, "venom"),
					JsonUtils.getOptionalFloat(pObj, "jawStrength"),
					JsonUtils.getOptionalFloat(pObj, "stealth"));
		}

		@Override
		protected String targetTypeName() {
			return DragonExternalValues.class.getSimpleName();
		}
	}
}
