/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.dragon.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;
import java.util.List;

public record DragonExternalStats(
		List<String> notes,
		DragonExternalValues values) {

	public static JsonDeserializer<DragonExternalStats> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonExternalStats> {

		@Override
		protected DragonExternalStats deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonExternalStats(
					JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(pObj, "notes")),
					GsonHelper.getAsObject(pObj, "values", pContext, DragonExternalValues.class));
		}

		@Override
		protected String targetTypeName() {
			return DragonExternalStats.class.getSimpleName();
		}
	}
}
