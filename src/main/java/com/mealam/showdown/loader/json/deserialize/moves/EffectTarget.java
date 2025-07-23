/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.moves;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mealam.showdown.utils.json.JsonObjectExtensionsKt;

public record EffectTarget(
		String status,
		String type,
		Integer value,
		Integer chance) {

	public static JsonDeserializer<EffectTarget> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String status = JsonObjectExtensionsKt.getAsString(obj, "status");
			String type = JsonObjectExtensionsKt.getAsString(obj, "type");
			Integer value = JsonObjectExtensionsKt.getAsInt(obj, "value");
			Integer chance = JsonObjectExtensionsKt.getAsInt(obj, "chance");

			return new EffectTarget(
					status,
					type,
					value,
					chance);
		};
	}
}
