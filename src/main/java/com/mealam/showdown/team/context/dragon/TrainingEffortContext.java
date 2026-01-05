/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.context.dragon;

import java.util.Map;

public record TrainingEffortContext(
		Map<String, Integer> values) {

	public TrainingEffortContext {
		if (values == null) {
			values = Map.of();
		} else {
			// defensive copy and clamp values to 0..255
			var copy = new java.util.HashMap<String, Integer>(values.size());
			for (var e : values.entrySet()) {
				int v = e.getValue() == null ? 0 : Math.max(0, Math.min(255, e.getValue()));
				copy.put(e.getKey(), v);
			}
			values = java.util.Map.copyOf(copy);
		}
	}
}
