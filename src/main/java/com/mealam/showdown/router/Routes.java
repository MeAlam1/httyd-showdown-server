package com.mealam.showdown.router;

import com.mealam.showdown.api.StaticAPIRouter;
import com.mealam.showdown.battle.BattleRouter;
import io.javalin.Javalin;

public class Routes {

	public static void register(Javalin pApp) {
		StaticAPIRouter.register(pApp);
		BattleRouter.register(pApp);
	}
}
