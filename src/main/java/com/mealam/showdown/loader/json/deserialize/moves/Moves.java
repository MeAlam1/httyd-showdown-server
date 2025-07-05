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

public record Moves(
		String id,
		String name,
		String description,
		String type,
		String category,
		Integer power,
		Integer accuracy,
		Integer pp,
		Effect effect

) {

	public static JsonDeserializer<Moves> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String id = JsonObjectExtensionsKt.getAsString(obj, "id");
			String name = JsonObjectExtensionsKt.getAsString(obj, "name");
			String description = JsonObjectExtensionsKt.getAsString(obj, "description");
			String type = JsonObjectExtensionsKt.getAsString(obj, "type");
			String category = JsonObjectExtensionsKt.getAsString(obj, "category");
			Integer power = JsonObjectExtensionsKt.getAsInt(obj, "power");
			Integer accuracy = JsonObjectExtensionsKt.getAsInt(obj, "accuracy");
			Integer pp = JsonObjectExtensionsKt.getAsInt(obj, "pp");
			Effect effect = JsonObjectExtensionsKt.getAsObject(obj, "effect", context, Effect.class);

			return new Moves(
					id,
					name,
					description,
					type,
					category,
					power,
					accuracy,
					pp,
					effect);
		};
	}
}
