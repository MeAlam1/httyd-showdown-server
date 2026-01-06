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
 * Immutable value object representing an event emitted by a script.
 *
 * <p>The record stores a non-null string {@code id} and a defensive, immutable copy
 * of the provided {@code payload} map. Consumers may inspect the payload safely
 * without affecting the original map passed by the script.</p>
 */
public record EmittedEvent(@NotNull String id, @NotNull Map<String, Object> payload) {

	public EmittedEvent {
		payload = Map.copyOf(payload);
	}
}
