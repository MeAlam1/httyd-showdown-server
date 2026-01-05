package com.mealam.showdown.loader.json.deserialize.nature;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.common.Metadata;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record Nature(
		String schemaVersion,
		String id,
		String name,
		String description,
		String type,
		List<String> tags,
		NatureStatMultipliers statMultipliers,
		NatureGrowthCurves growthCurves,
		List<NaturePassiveEffectsEntry> passiveEffects,
		Metadata metadata
) {
	public static JsonDeserializer<Nature> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Nature> {
		@Override
		protected Nature deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Nature(
					GsonHelper.getAsString(pObj, "schemaVersion"),
					GsonHelper.getAsString(pObj, "id"),
					GsonHelper.getAsString(pObj, "name"),
					GsonHelper.getAsString(pObj, "description"),
					GsonHelper.getAsString(pObj, "type"),
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "tags")),
					GsonHelper.getAsObject(pObj, "statMultipliers", pContext, NatureStatMultipliers.class),
					GsonHelper.getAsObject(pObj, "growthCurves", pContext, NatureGrowthCurves.class),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "passiveEffects"), pContext, NaturePassiveEffectsEntry.class),
					GsonHelper.getAsObject(pObj, "metadata", pContext, Metadata.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return Nature.class.getSimpleName();
		}
	}
}
