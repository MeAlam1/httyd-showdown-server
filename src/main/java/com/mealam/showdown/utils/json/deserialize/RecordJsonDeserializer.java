/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.json.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public abstract class RecordJsonDeserializer<T> implements JsonDeserializer<T> {

	@Override
	public final T deserialize(JsonElement pJson, Type pTypeOfT, JsonDeserializationContext pContext)
			throws JsonParseException {
		try {
			if (pJson == null || pJson.isJsonNull()) {
				throw new JsonParseException("Expected JSON object but got null");
			}

			JsonObject obj = pJson.getAsJsonObject();
			return deserializeObject(obj, pContext);
		} catch (Exception pException) {
			if (pException instanceof JsonParseException jpe) throw jpe;
			throw new JsonParseException("Failed to deserialize " + targetTypeName() + ": " + pException.getMessage(), pException);
		}
	}

	protected abstract T deserializeObject(JsonObject pObj, JsonDeserializationContext pContext);

	protected String targetTypeName() {
		return getClass().getSimpleName();
	}
}
