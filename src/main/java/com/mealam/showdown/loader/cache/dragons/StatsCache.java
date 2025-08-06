/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.cache.dragons;

public record StatsCache(
		int attack,
		int speed,
		int armor,
		int firepower,
		int shotLimit,
		int venom,
		int jawStrength,
		int stealth) {}
