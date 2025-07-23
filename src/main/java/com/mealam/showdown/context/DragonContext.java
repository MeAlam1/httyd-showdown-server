/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.context;

import com.mealam.showdown.loader.cache.dragons.DragonsCache;
import com.mealam.showdown.move.enums.Status;
import java.util.List;

public record DragonContext(
		DragonsCache baseData,
		String dragonId,
		String name,
		int level,
		List<MoveContext> moves,
		int currentHp,
		int maxHp,
		Status status) {}
