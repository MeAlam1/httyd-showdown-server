/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonPhysicalStats;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonStats;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record Dragon(
		String schemaVersion,
		String id,
		String name,
		boolean trainable,
		DragonClassification classification,
		DragonAppearances appearances,
		DragonBiology biology,
		DragonAbilities abilities,
		List<String> moveset,
		DragonPhysicalStats physicalStats,
		List<String> individuals,
		List<String> subspecies,
		List<String> hybrids,
		DragonStats stats,
		DragonMedia media,
		List<String> tags,
		DragonMetadata metadata) {

	public static JsonDeserializer<Dragon> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Dragon> {

		@Override
		protected Dragon deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Dragon(
					GsonHelper.getAsString(pObj, "schemaVersion"),
					GsonHelper.getAsString(pObj, "id"),
					GsonHelper.getAsString(pObj, "name"),
					GsonHelper.getAsBoolean(pObj, "trainable"),
					GsonHelper.getAsObject(pObj, "classification", pContext, DragonClassification.class),
					GsonHelper.getAsObject(pObj, "appearances", pContext, DragonAppearances.class),
					GsonHelper.getAsObject(pObj, "biology", pContext, DragonBiology.class),
					GsonHelper.getAsObject(pObj, "abilities", pContext, DragonAbilities.class),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "moveset")),
					GsonHelper.getAsObject(pObj, "physicalStats", pContext, DragonPhysicalStats.class),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "individuals")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "subspecies")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "hybrids")),
					GsonHelper.getAsObject(pObj, "stats", pContext, DragonStats.class),
					GsonHelper.getAsObject(pObj, "media", pContext, DragonMedia.class),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "tags")),
					GsonHelper.getAsObject(pObj, "metadata", pContext, DragonMetadata.class));
		}

		@Override
		protected String targetTypeName() {
			return Dragon.class.getSimpleName();
		}
	}
}
