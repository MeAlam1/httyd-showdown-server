/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.api;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;

public interface BattleRepository {

	BattleContext save(BattleContext pBattle);

	BattleContext get(BattleId pBattleId);

	void update(BattleContext pBattle);
}
