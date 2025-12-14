package com.mealam.showdown.battle.api;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;

public interface BattleRepository {
	BattleContext save(BattleContext pBattle);

	BattleContext get(BattleId pBattleId);

	void update(BattleContext pBattle);
}