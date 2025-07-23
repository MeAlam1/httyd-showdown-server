/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragons;

import java.util.List;

public record DragonsCache(
		String id,
		String name,
		String img,
		String origin,
		List<String> classes,
		int attack,
		int speed,
		int armor,
		int firepower,
		int shot_limit,
		int venom,
		int jaw_strength,
		int stealth) {}
