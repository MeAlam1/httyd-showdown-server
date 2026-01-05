/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.runtime;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an emitted event.
 */
public record EmittedEvent(@NotNull String id, @NotNull Map<String, Object> payload) {

	public EmittedEvent {
		payload = Map.copyOf(payload);
	}
}
