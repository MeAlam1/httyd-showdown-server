/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.user.data;

import com.mealam.showdown.api.utils.IdGenerator;
import com.mealam.showdown.data.BaseId;

public class UserId extends BaseId {

	private UserId(String pValue) {
		super(pValue);
	}

	public static UserId generate() {
		return new UserId(IdGenerator.generateId());
	}

	public static UserId from(String pRaw) {
		return new UserId(pRaw);
	}
}
