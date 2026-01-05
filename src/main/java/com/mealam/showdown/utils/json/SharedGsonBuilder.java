package com.mealam.showdown.utils.json;


import com.google.gson.GsonBuilder;
import com.mealam.showdown.loader.json.deserialize.common.Measurement;
import com.mealam.showdown.loader.json.deserialize.common.Metadata;
import com.mealam.showdown.loader.json.deserialize.common.NamedResource;
import org.jetbrains.annotations.NotNull;

public final class SharedGsonBuilder {

	private SharedGsonBuilder() {
	}

	@NotNull
	public static GsonBuilder builder() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.setLenient()
				.registerTypeAdapter(Metadata.class, Metadata.deserializer())
				.registerTypeAdapter(NamedResource.class, NamedResource.deserializer())
				.registerTypeAdapter(Measurement.class, Measurement.deserializer());
	}
}