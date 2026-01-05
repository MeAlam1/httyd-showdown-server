/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mealam.showdown.data.BaseId;
import com.mealam.showdown.utils.IdGenerator;

public class TeamId extends BaseId {

	@JsonCreator
	public TeamId(String pValue) {
		super(pValue);
	}

	public static TeamId generate() {
		return new TeamId(IdGenerator.generateId());
	}

	public static TeamId parse(String pRaw) {
		return new TeamId(pRaw);
	}
}
