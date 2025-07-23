/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data;

import com.mealam.showdown.api.utils.ExtensibleEnum;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Phase extends ExtensibleEnum<Phase> {

	protected Phase(@NotNull String pName) {
		super(pName);
	}

	@NotNull
	public static final Registry<Phase> REGISTRY = new Registry<>() {

		@NotNull
		private final Map<String, Phase> map = new Object2ObjectOpenHashMap<>();

		@Override
		public @NotNull Map<String, Phase> versions() {
			return map;
		}

		@Override
		public @NotNull Phase defaultVersion() {
			return PhaseEnum.START.getPhase();
		}
	};

	@Getter
	private enum PhaseEnum {

		START("start"),
		IN_PROGRESS("in_progress"),
		FINISHED("finished");

		private final Phase phase;

		PhaseEnum(String pName) {
			this.phase = new Phase(pName);
			REGISTRY.register(phase);
		}
	}
}
