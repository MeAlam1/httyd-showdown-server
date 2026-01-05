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
import com.mealam.showdown.loader.json.deserialize.common.Measurement;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record DragonPhysicalStats(
		Measurement length,
		Measurement weight,
		Measurement wingspan) {

	public static JsonDeserializer<DragonPhysicalStats> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonPhysicalStats> {

		@Override
		protected DragonPhysicalStats deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonPhysicalStats(
					GsonHelper.getAsObject(pObj, "length", pContext, Measurement.class),
					GsonHelper.getAsObject(pObj, "weight", pContext, Measurement.class),
					GsonHelper.getAsObject(pObj, "wingspan", pContext, Measurement.class));
		}

		@Override
		protected String targetTypeName() {
			return DragonPhysicalStats.class.getSimpleName();
		}
	}
}
