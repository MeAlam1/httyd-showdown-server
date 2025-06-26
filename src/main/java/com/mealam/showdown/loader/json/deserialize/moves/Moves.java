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
import com.mealam.showdown.moves.Category;
import com.mealam.showdown.moves.Type;
import com.mealam.showdown.utils.json.GsonHelper;

public record Moves(
		String id,
		String name,
		String description,
		Type type,
		Category category,
		Integer power,
		Integer accuracy,
		Integer pp,
		Effect effect

) {

	public static JsonDeserializer<Moves> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String id = GsonHelper.getAsString(obj, "id");
			String name = GsonHelper.getAsString(obj, "name");
			String description = GsonHelper.getAsString(obj, "description");
			Type type = GsonHelper.getAsObject(obj, "type", context, Type.class);
			Category category = GsonHelper.getAsObject(obj, "type", context, Category.class);
			Integer power = GsonHelper.getAsInt(obj, "power");
			Integer accuracy = GsonHelper.getAsInt(obj, "accuracy");
			Integer pp = GsonHelper.getAsInt(obj, "pp");
			Effect effect = GsonHelper.getAsObject(obj, "effect", context, Effect.class);

			return new Moves(
					id,
					name,
					description,
					type,
					category,
					power,
					accuracy,
					pp,
					effect
			);
		};
	}
}
