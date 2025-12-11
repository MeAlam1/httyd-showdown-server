package com.mealam.showdown.battle;

import com.mealam.showdown.api.StaticAPIRouter;
import io.javalin.Javalin;

public class BattleRouter {

	private static final BattleController battle = new BattleController();

	public static void register(Javalin pApp) {
		pApp.post("/battle/start", battle::startBattle);
		pApp.get("/battle/{id}", battle::getBattle);

		pApp.ws("/ws/battle/{id}", BattleWebSocket::configure);
	}
}
