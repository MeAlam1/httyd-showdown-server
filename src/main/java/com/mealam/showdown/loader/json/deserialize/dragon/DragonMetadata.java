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
import com.mealam.showdown.loader.json.deserialize.common.Metadata;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record DragonMetadata(
		boolean canonical,
		Metadata metadata) {

	public static JsonDeserializer<DragonMetadata> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonMetadata> {

		@Override
		protected DragonMetadata deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			String introducedIn = GsonHelper.getAsString(pObj, "introducedIn");
			String lastUpdated = GsonHelper.getAsString(pObj, "lastUpdated");
			return new DragonMetadata(
					GsonHelper.getAsBoolean(pObj, "canonical"),
					new Metadata(introducedIn, lastUpdated));
		}

		@Override
		protected String targetTypeName() {
			return DragonMetadata.class.getSimpleName();
		}
	}
}
