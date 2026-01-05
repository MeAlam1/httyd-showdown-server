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
import com.mealam.showdown.loader.json.deserialize.common.Metadata;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record Ability(
		String schemaVersion,
		String id,
		String name,
		String description,
		String type,
		String target,
		List<AbilityEffectsEntry> effects,
		AbilityScaling scaling,
		AbilityConditions conditions,
		Metadata metadata) {

	public static JsonDeserializer<Ability> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Ability> {

		@Override
		protected Ability deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Ability(
					GsonHelper.getAsString(pObj, "schemaVersion"),
					GsonHelper.getAsString(pObj, "id"),
					GsonHelper.getAsString(pObj, "name"),
					GsonHelper.getAsString(pObj, "description"),
					GsonHelper.getAsString(pObj, "type"),
					GsonHelper.getAsString(pObj, "target"),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "effects"), pContext, AbilityEffectsEntry.class),
					GsonHelper.getAsObject(pObj, "scaling", pContext, AbilityScaling.class),
					GsonHelper.getAsObject(pObj, "conditions", pContext, AbilityConditions.class),
					GsonHelper.getAsObject(pObj, "metadata", pContext, Metadata.class));
		}

		@Override
		protected String targetTypeName() {
			return Ability.class.getSimpleName();
		}
	}
}
