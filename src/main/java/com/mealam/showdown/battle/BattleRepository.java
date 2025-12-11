package com.mealam.showdown.battle;


import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BattleRepository {

	private final Map<BattleId, BattleContext> battles = new ConcurrentHashMap<>();

	public BattleContext save(BattleContext battle) {
		battles.put(battle.battleId(), battle);
		return battle;
	}

	public BattleContext get(BattleId id) {
		return battles.get(id);
	}

	public void update(BattleContext battle) {
		battles.put(battle.battleId(), battle);
	}
}
