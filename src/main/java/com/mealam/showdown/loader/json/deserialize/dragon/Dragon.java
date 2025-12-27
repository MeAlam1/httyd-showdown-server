package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.DragonStats;
import com.mealam.showdown.loader.json.deserialize.dragon.stats.PhysicalStats;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record Dragon(
		String schemaVersion,
		String id,
		String name,
		boolean trainable,
		Classification classification,
		Appearances appearances,
		Biology biology,
		Abilities abilities,
		PhysicalStats physicalStats,
		List<String> individuals,
		List<String> subspecies,
		List<String> hybrids,
		DragonStats stats,
		Media media,
		List<String> tags,
		Metadata metadata
) {
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
					GsonHelper.getAsObject(pObj, "classification", pContext, Classification.class),
					GsonHelper.getAsObject(pObj, "appearances", pContext, Appearances.class),
					GsonHelper.getAsObject(pObj, "biology", pContext, Biology.class),
					GsonHelper.getAsObject(pObj, "abilities", pContext, Abilities.class),
					GsonHelper.getAsObject(pObj, "physicalStats", pContext, PhysicalStats.class),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "individuals")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "subspecies")),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "hybrids")),
					GsonHelper.getAsObject(pObj, "stats", pContext, DragonStats.class),
					GsonHelper.getAsObject(pObj, "media", pContext, Media.class),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "tags")),
					GsonHelper.getAsObject(pObj, "metadata", pContext, Metadata.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return Dragon.class.getSimpleName();
		}
	}
}