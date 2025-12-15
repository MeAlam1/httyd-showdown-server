package com.mealam.showdown.battle.http;

import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.domain.DefaultBattleService;
import com.mealam.showdown.battle.infra.FileBattleRepository;
import com.mealam.showdown.battle.infra.DefaultBattleRepository;
import com.mealam.showdown.battle.infra.InMemoryBattleRepository;
import com.mealam.showdown.battle.infra.ws.BattleWebSocket;
import com.mealam.showdown.battle.party.PartyService;
import io.javalin.Javalin;

import java.nio.file.Path;

public class BattleRouter {

	private final BattleController battleController;

	public BattleRouter(BattleService pService) {
		this.battleController = new BattleController(pService);
	}

	public static void configure(Javalin pApp) {
		Path root = Path.of("D:\\Personal\\httyd-showdown-server");

		InMemoryBattleRepository mem = new InMemoryBattleRepository();
		FileBattleRepository file = new FileBattleRepository(root);
		DefaultBattleRepository hybrid = new DefaultBattleRepository(mem, file);

		BattleService service = new DefaultBattleService(
				hybrid,
				new PartyService()
		);

		new BattleRouter(service).register(pApp);
		pApp.ws("/ws/battle/{id}", new BattleWebSocket(service)::configure);
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