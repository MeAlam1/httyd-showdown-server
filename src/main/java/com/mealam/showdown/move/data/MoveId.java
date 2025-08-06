/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.move.data;

import com.mealam.showdown.api.utils.IdGenerator;
import com.mealam.showdown.data.BaseId;

public class MoveId extends BaseId {

	private MoveId(String pValue) {
		super(pValue);
	}

	public static MoveId generate() {
		return new MoveId(IdGenerator.generateId());
	}

	public static MoveId parse(String pRaw) {
		return new MoveId(pRaw);
	}
}
