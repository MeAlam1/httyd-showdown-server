/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.common.Metadata;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record Item(
		String schemaVersion,
		String id,
		String name,
		String description,
		String icon,
		List<ItemEffectsEntry> effects,
		ItemConditions conditions,
		Metadata metadata) {

	public static JsonDeserializer<Item> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Item> {

		@Override
		protected Item deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Item(
					GsonHelper.getAsString(pObj, "schemaVersion"),
					GsonHelper.getAsString(pObj, "id"),
					GsonHelper.getAsString(pObj, "name"),
					GsonHelper.getAsString(pObj, "description"),
					GsonHelper.getAsString(pObj, "icon"),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "effects"), pContext, ItemEffectsEntry.class),
					GsonHelper.getAsObject(pObj, "conditions", pContext, ItemConditions.class),
					GsonHelper.getAsObject(pObj, "metadata", pContext, Metadata.class));
		}

		@Override
		protected String targetTypeName() {
			return Item.class.getSimpleName();
		}
	}
}
