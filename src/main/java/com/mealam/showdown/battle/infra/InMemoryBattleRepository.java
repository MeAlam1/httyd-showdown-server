/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.infra;

import com.mealam.showdown.battle.api.BattleRepository;
import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBattleRepository implements BattleRepository {

	private final Map<BattleId, BattleContext> battles = new ConcurrentHashMap<>();

	@Override
	public BattleContext save(BattleContext pBattle) {
		battles.put(pBattle.battleId(), pBattle);
		return pBattle;
	}

	@Override
	public BattleContext get(BattleId pBattleId) {
		return battles.get(pBattleId);
	}

	@Override
	public void update(BattleContext pBattle) {
		battles.put(pBattle.battleId(), pBattle);
	}
}
