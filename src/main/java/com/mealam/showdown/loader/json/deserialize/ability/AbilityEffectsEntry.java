/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.ability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record AbilityEffectsEntry(
		String effectId,
		AbilityEffectsEntryParameters parameters) {

	public static JsonDeserializer<AbilityEffectsEntry> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityEffectsEntry> {

		@Override
		protected AbilityEffectsEntry deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityEffectsEntry(
					GsonHelper.getAsString(pObj, "effectId"),
					GsonHelper.getAsObject(pObj, "parameters", pContext, AbilityEffectsEntryParameters.class));
		}

		@Override
		protected String targetTypeName() {
			return AbilityEffectsEntry.class.getSimpleName();
		}
	}
}
