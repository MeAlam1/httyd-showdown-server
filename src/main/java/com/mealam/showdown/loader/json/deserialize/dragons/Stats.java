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
import com.mealam.showdown.utils.json.JsonUtils;
import org.jetbrains.annotations.Nullable;

public record Stats(
		@Nullable Float attack,
		@Nullable Float speed,
		@Nullable Float armor,
		@Nullable Float firePower,
		@Nullable Float shotLimit,
		@Nullable Float venom,
		@Nullable Float jawStrength,
		@Nullable Float stealth) {

	public static JsonDeserializer<Stats> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Float attack = JsonUtils.getOptionalFloat(obj, "attack");
			Float speed = JsonUtils.getOptionalFloat(obj, "speed");
			Float armor = JsonUtils.getOptionalFloat(obj, "armor");
			Float firePower = JsonUtils.getOptionalFloat(obj, "firepower");
			Float shotLimit = JsonUtils.getOptionalFloat(obj, "shotLimit");
			Float venom = JsonUtils.getOptionalFloat(obj, "venom");
			Float jawStrength = JsonUtils.getOptionalFloat(obj, "jawStrength");
			Float stealth = JsonUtils.getOptionalFloat(obj, "stealth");

			return new Stats(
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
