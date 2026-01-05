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
import com.mealam.showdown.battle.data.TurnManager;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import java.util.Objects;

public class DefaultBattleRepository implements BattleRepository {

	private final InMemoryBattleRepository memRepo;
	private final FileBattleRepository fileRepo;

	public DefaultBattleRepository(InMemoryBattleRepository pMemRepo, FileBattleRepository pFileRepo) {
		this.memRepo = Objects.requireNonNull(pMemRepo);
		this.fileRepo = Objects.requireNonNull(pFileRepo);
		BaseLogger.log(BaseLogLevel.INFO, "HybridBattleRepository initialized");
	}

	@Override
	public BattleContext save(BattleContext pBattle) {
		BaseLogger.log(BaseLogLevel.INFO, "Hybrid.save -> memory for battleId=" + pBattle.battleId());
		memRepo.save(pBattle);
		conditionallyPersist(pBattle);
		return pBattle;
	}

	@Override
	public BattleContext get(BattleId pBattleId) {
		BattleContext ctx = memRepo.get(pBattleId);
		if (ctx != null) {
			BaseLogger.log(BaseLogLevel.INFO, "Hybrid.get -> found in memory for battleId=" + pBattleId);
			return ctx;
		}
		BaseLogger.log(BaseLogLevel.INFO, "Hybrid.get -> not in memory, checking file for battleId=" + pBattleId);
		ctx = fileRepo.get(pBattleId);
		if (ctx != null) {
			BaseLogger.log(BaseLogLevel.INFO, "Hybrid.get -> loaded from file, caching in memory for battleId=" + pBattleId);
			memRepo.save(ctx);
		} else {
			BaseLogger.log(BaseLogLevel.WARNING, "Hybrid.get -> battle not found anywhere for battleId=" + pBattleId);
		}
		return ctx;
	}

	@Override
	public void update(BattleContext pBattle) {
		BaseLogger.log(BaseLogLevel.INFO, "Hybrid.update -> memory for battleId=" + pBattle.battleId());
		memRepo.update(pBattle);
		conditionallyPersist(pBattle);
	}

	private void conditionallyPersist(BattleContext pBattle) {
		boolean finished = pBattle.turnContext() != null && pBattle.turnContext().turnNumber() == TurnManager.FINISHED;
		pBattle.getAllPlayerIds();
		boolean noPlayers = pBattle.getAllPlayerIds().isEmpty();

		if (finished || noPlayers) {
			BaseLogger.log(BaseLogLevel.INFO, "Hybrid.persist -> writing to file for battleId=" + pBattle.battleId()
					+ " finished=" + finished + " noPlayers=" + noPlayers);
			fileRepo.update(pBattle);
		} else {
			BaseLogger.log(BaseLogLevel.INFO, "Hybrid.persist -> staying in memory for battleId=" + pBattle.battleId());
		}
	}
}
