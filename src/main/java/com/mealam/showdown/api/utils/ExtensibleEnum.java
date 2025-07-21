package com.mealam.showdown.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public abstract class ExtensibleEnum<T extends ExtensibleEnum<T>> {

	@NotNull
	private final String serializedName;

	protected ExtensibleEnum(@NotNull String pName) {
		this.serializedName = pName;
	}

	public @NotNull String getSerializedName() {
		return serializedName;
	}

	@NotNull
	protected static <T extends ExtensibleEnum<T>> T register(@NotNull Map<String, T> pRegistry, @NotNull T pValue) {
		pRegistry.put(pValue.getSerializedName(), pValue);
		return pValue;
	}

	public interface Registry<T extends ExtensibleEnum<T>> {

		@NotNull
		Map<String, T> versions();

		@NotNull
		T defaultVersion();

		default @NotNull T get(@NotNull String pName) {
			return versions().getOrDefault(pName, defaultVersion());
		}

		default void register(@NotNull T pValue) {
			versions().put(pValue.getSerializedName(), pValue);
		}

		default @NotNull Map<String, T> getRegisteredVersions() {
			return Collections.unmodifiableMap(versions());
		}

		default @NotNull T match(@NotNull String pVersion) {
			return get(pVersion);
		}
	}
}
