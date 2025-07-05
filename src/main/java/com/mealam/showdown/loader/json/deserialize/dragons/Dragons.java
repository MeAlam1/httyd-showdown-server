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

import java.util.List;
import org.jetbrains.annotations.Nullable;

public record Dragons(
		String id,
		String name,
		@Nullable String img,
		String origin,
		List<String> classes,
		@Nullable Float attack,
		@Nullable Float speed,
		@Nullable Float armor,
		@Nullable Float firePower,
		@Nullable Float shotLimit,
		@Nullable Float venom,
		@Nullable Float jawStrength,
		@Nullable Float stealth) {

	public static JsonDeserializer<Dragons> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String id = JsonObjectExtensionsKt.getAsString(obj, "id");
			String name = JsonObjectExtensionsKt.getAsString(obj, "name");
			String img = JsonObjectExtensionsKt.getOptionalString(obj, "img");
			String origin = JsonObjectExtensionsKt.getAsString(obj, "origin");
			List<String> classes = JsonArrayExtensionsKt.toStringList(JsonObjectExtensionsKt.getAsJsonArrayKt(obj, "classes"));
			Float attack = JsonObjectExtensionsKt.getOptionalFloat(obj, "attack");
			Float speed = JsonObjectExtensionsKt.getOptionalFloat(obj, "speed");
			Float armor = JsonObjectExtensionsKt.getOptionalFloat(obj, "armor");
			Float firePower = JsonObjectExtensionsKt.getOptionalFloat(obj, "firepower");
			Float shotLimit = JsonObjectExtensionsKt.getOptionalFloat(obj, "shot_limit");
			Float venom = JsonObjectExtensionsKt.getOptionalFloat(obj, "venom");
			Float jawStrength = JsonObjectExtensionsKt.getOptionalFloat(obj, "jaw_strength");
			Float stealth = JsonObjectExtensionsKt.getOptionalFloat(obj, "stealth");

			return new Dragons(
					id,
					name,
					img,
					origin,
					classes,
					attack,
					speed,
					armor,
					firePower,
					shotLimit,
					venom,
					jawStrength,
					stealth);
		};
	}
}
