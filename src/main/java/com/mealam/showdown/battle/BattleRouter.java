package com.mealam.showdown.battle;

import io.javalin.Javalin;

public class BattleRouter {

	private static final BattleController battle = new BattleController();

	public static void register(Javalin pApp) {
		pApp.post("/battle/create", battle::createBattle);
		pApp.post("/battle/{id}/start", battle::startBattle);
		pApp.post("/battle/{id}/turn", battle::advanceTurn);
		pApp.post("/battle/{id}/finish", battle::finishBattle);
		pApp.post("/battle/{id}/join", battle::joinBattle);
		pApp.post("/battle/{id}/leave", battle::leaveBattle);
		pApp.get("/battle/{id}", battle::getBattle);

		pApp.ws("/ws/battle/{id}", BattleWebSocket::configure);
	}
}
