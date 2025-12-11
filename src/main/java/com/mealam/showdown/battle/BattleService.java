package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;

import java.util.UUID;

public class BattleService {

	private final BattleRepository repo = new BattleRepository();

	public BattleContext createBattle(CreateBattleRequest pRequest) {
		var battle = new BattleContext(
				BattleId.generate(),
				null,
				null,
				null,
				Phase.REGISTRY.defaultVersion(),
				null

		);

		repo.save(battle);
		return battle;
	}

	public BattleContext getBattle(BattleId pId) {
		return repo.get(pId);
	}

	public void applyStateChange(BattleContext pBattle) {
		repo.update(pBattle);
	}
}
