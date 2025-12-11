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
import java.util.List;

import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import org.jetbrains.annotations.Nullable;

public record Dragons(
		String id,
		String name,
		@Nullable String image,
		String origin,
		List<String> classes,
		@Nullable Stats stats,
		List<String> learnSet) {

	public static JsonDeserializer<Dragons> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String id = GsonHelper.getAsString(obj, "id");
			String name = GsonHelper.getAsString(obj, "name");
			String image = JsonUtils.getOptionalString(obj, "image");
			String origin = GsonHelper.getAsString(obj, "origin");
			List<String> classes = JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(obj, "classes"));
			Stats stats = GsonHelper.convertToObject(obj, "stats", context, Stats.class);
			List<String> learnSet = JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(obj, "learnSet"));

			return new Dragons(
					id,
					name,
					image,
					origin,
					classes,
					stats,
					learnSet);
		};
	}
}
