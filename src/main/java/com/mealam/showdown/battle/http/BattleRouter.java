package com.mealam.showdown.battle.http;

import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.domain.DefaultBattleService;
import com.mealam.showdown.battle.infra.InMemoryBattleRepository;
import com.mealam.showdown.battle.infra.ws.BattleWebSocket;
import com.mealam.showdown.battle.party.PartyService;
import io.javalin.Javalin;

public class BattleRouter {

	private final BattleController battleController;

	public BattleRouter(BattleService pService) {
		this.battleController = new BattleController(pService);
	}

	public static void configure(Javalin pApp) {
		BattleService service = new DefaultBattleService(
				new InMemoryBattleRepository(),
				new PartyService()
		);

		new BattleRouter(service).register(pApp);
		pApp.ws("/ws/battle/{id}", new BattleWebSocket(service)::configure);
	}

	public void register(Javalin app) {
		app.post("/battle/create", battleController::createBattle);
		app.post("/battle/{id}/start", battleController::startBattle);
		app.post("/battle/{id}/turn", battleController::advanceTurn);
		app.post("/battle/{id}/finish", battleController::finishBattle);
		app.post("/battle/{id}/join", battleController::joinBattle);
		app.post("/battle/{id}/leave", battleController::leaveBattle);
		app.get("/battle/{id}", battleController::getBattle);
	}
}