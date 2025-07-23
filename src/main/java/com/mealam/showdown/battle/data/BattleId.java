/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data;

import com.mealam.showdown.api.utils.IdGenerator;
import com.mealam.showdown.data.BaseId;

public class BattleId extends BaseId {

	private BattleId(String pValue) {
		super(pValue);
	}

	public static BattleId generate() {
		return new BattleId(IdGenerator.generateId());
	}

	public static BattleId parse(String pRaw) {
		return new BattleId(pRaw);
	}
}
