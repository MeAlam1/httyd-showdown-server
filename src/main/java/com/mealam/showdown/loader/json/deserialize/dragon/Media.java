package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.common.NamedResource;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record Media(
		List<NamedResource> images,
		List<NamedResource> videos,
		List<NamedResource> sounds
) {
	public static JsonDeserializer<Media> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<Media> {
		@Override
		protected Media deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new Media(
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "images"), pContext, NamedResource.class),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "videos"), pContext, NamedResource.class),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "sounds"), pContext, NamedResource.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return Media.class.getSimpleName();
		}
	}
}
