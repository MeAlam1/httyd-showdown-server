/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.http;

import com.mealam.showdown.Main;
import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.domain.DefaultBattleService;
import com.mealam.showdown.battle.infra.DefaultBattleRepository;
import com.mealam.showdown.battle.infra.FileBattleRepository;
import com.mealam.showdown.battle.infra.InMemoryBattleRepository;
import com.mealam.showdown.battle.infra.ws.BattleWebSocket;
import com.mealam.showdown.battle.party.PartyService;
import io.javalin.Javalin;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BattleRouter {

	private final BattleController battleController;

	public BattleRouter(BattleService pService) {
		this.battleController = new BattleController(pService);
	}

	public static void configure(Javalin pApp) {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");

			InMemoryBattleRepository mem = new InMemoryBattleRepository();
			FileBattleRepository file = new FileBattleRepository(dataRoot);
			DefaultBattleRepository hybrid = new DefaultBattleRepository(mem, file);

			BattleService service = new DefaultBattleService(
					hybrid,
					new PartyService());

			new BattleRouter(service).register(pApp);
			pApp.ws("/ws/battle/{id}", new BattleWebSocket(service)::configure);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare data dir", e);
		}
	}

	public void register(Javalin pApp) {
		pApp.post("/api/battle/create", battleController::createBattle);
		pApp.post("/api/battle/{id}/start", battleController::startBattle);
		pApp.post("/api/battle/{id}/turn", battleController::advanceTurn);
		pApp.post("/api/battle/{id}/finish", battleController::finishBattle);
		pApp.post("/api/battle/{id}/join", battleController::joinBattle);
		pApp.post("/api/battle/{id}/leave", battleController::leaveBattle);
		pApp.get("/api/battle/{id}", battleController::getBattle);
	}
}
