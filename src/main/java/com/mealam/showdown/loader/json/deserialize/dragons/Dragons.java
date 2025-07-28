/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.dragons;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mealam.showdown.utils.json.JsonArrayExtensionsKt;
import com.mealam.showdown.utils.json.JsonObjectExtensionsKt;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Dragons(
		String id,
		String name,
		@Nullable String img,
		String origin,
		List<String> classes,
		@Nullable Stats stats) {

	public static JsonDeserializer<Dragons> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String id = JsonObjectExtensionsKt.getAsString(obj, "id");
			String name = JsonObjectExtensionsKt.getAsString(obj, "name");
			String img = JsonObjectExtensionsKt.getOptionalString(obj, "img");
			String origin = JsonObjectExtensionsKt.getAsString(obj, "origin");
			List<String> classes = JsonArrayExtensionsKt.toStringList(JsonObjectExtensionsKt.getAsJsonArrayKt(obj, "classes"));
			Stats stats = JsonObjectExtensionsKt.convertToObject(obj, "stats", context, Stats.class);

			return new Dragons(
					id,
					name,
					img,
					origin,
					classes,
					stats);
		};
	}
}
