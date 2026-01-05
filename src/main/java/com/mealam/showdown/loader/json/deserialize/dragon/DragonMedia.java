package com.mealam.showdown.loader.json.deserialize.dragon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.loader.json.deserialize.common.NamedResource;
import com.mealam.showdown.utils.json.GsonHelper;
import com.mealam.showdown.utils.json.JsonUtils;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

import java.util.List;

public record DragonMedia(
		List<NamedResource> images,
		List<NamedResource> videos,
		List<NamedResource> sounds
) {
	public static JsonDeserializer<DragonMedia> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<DragonMedia> {
		@Override
		protected DragonMedia deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new DragonMedia(
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "images"), pContext, NamedResource.class),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "videos"), pContext, NamedResource.class),
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(pObj, "sounds"), pContext, NamedResource.class)
			);
		}

		@Override
		protected String targetTypeName() {
			return DragonMedia.class.getSimpleName();
		}
	}
}
