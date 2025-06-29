/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.moves;

import com.mealam.showdown.loader.json.FormatVersion;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MovesFormatVersion extends FormatVersion<MovesFormatVersion> {

	public static final Registry<MovesFormatVersion> REGISTRY = new Registry<>() {

		private final Map<String, MovesFormatVersion> map = new Object2ObjectOpenHashMap<>();
		private final MovesFormatVersion defaultVersion = new MovesFormatVersion("1.0.0", true, null);

		{
			register(defaultVersion);
		}

		@Override
		public Map<String, MovesFormatVersion> versions() {
			return map;
		}

		@Override
		public MovesFormatVersion defaultVersion() {
			return defaultVersion;
		}
	};

	protected MovesFormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
		super(pSerializedName, pSupported, pErrorMessage);
	}
}
