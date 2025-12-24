package com.mealam.showdown.router;

import com.mealam.showdown.api.StaticAPIRouter;
import com.mealam.showdown.battle.http.BattleRouter;
import com.mealam.showdown.user.http.UserRouter;
import io.javalin.Javalin;

public class Routes {

	public static void register(Javalin pApp) {
		StaticAPIRouter.register(pApp);
		BattleRouter.configure(pApp);
		UserRouter.configure(pApp);
	}
}
