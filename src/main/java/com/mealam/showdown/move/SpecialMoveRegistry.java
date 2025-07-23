/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.move;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpecialMoveRegistry {

	private static final Map<String, SpecialMoveEffect> registry = new ConcurrentHashMap<>();

	public static void register(String pMoveId, SpecialMoveEffect pEffect) {
		registry.put(pMoveId, pEffect);
	}

	public static SpecialMoveEffect get(String pMoveId) {
		return registry.get(pMoveId);
	}
}
