/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

public record NamedResource(
		String name,
		String url) {

	public static JsonDeserializer<NamedResource> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<NamedResource> {

		@Override
		protected NamedResource deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new NamedResource(
					GsonHelper.getAsString(pObj, "name"),
					GsonHelper.getAsString(pObj, "url"));
		}

		@Override
		protected String targetTypeName() {
			return NamedResource.class.getSimpleName();
		}
	}
}
