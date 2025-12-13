package com.mealam.showdown.battle;


import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BattleRepository {

	private final Map<BattleId, BattleContext> battles = new ConcurrentHashMap<>();

	public BattleContext save(BattleContext pBattle) {
		battles.put(pBattle.battleId(), pBattle);
		return pBattle;
	}

	public BattleContext get(BattleId pId) {
		return battles.get(pId);
	}

	public void update(BattleContext pBattle) {
		battles.put(pBattle.battleId(), pBattle);
	}
}
