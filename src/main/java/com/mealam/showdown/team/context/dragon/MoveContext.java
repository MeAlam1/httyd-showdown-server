/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.context.dragon;

public record MoveContext(
		String moveId,
		int slot) {

	public MoveContext {
		if (slot < 1) slot = 1;
		if (slot > 4) slot = 4;
	}
}
