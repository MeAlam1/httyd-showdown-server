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
import com.mealam.showdown.utils.json.JsonArrayExtensionsKt;
import com.mealam.showdown.utils.json.JsonObjectExtensionsKt;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record Effect(
		@Nullable List<EffectTarget> self,
		@Nullable List<EffectTarget> effect) {

	public static JsonDeserializer<Effect> deserializer() throws JsonParseException {
		return (json, pType, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<EffectTarget> self = JsonArrayExtensionsKt.toObjectList(JsonObjectExtensionsKt.getOptionalJsonArray(obj, "self"), context, EffectTarget.class);
			List<EffectTarget> effect = JsonArrayExtensionsKt.toObjectList(JsonObjectExtensionsKt.getOptionalJsonArray(obj, "enemy"), context, EffectTarget.class);

			return new Effect(
					self,
					effect);
		};
	}
}
