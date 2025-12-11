package com.mealam.showdown.config;

import com.mealam.showdown.api.StaticAPIRouter;
import io.javalin.Javalin;

public class Routes {

	public static void register(Javalin pApp) {
		StaticAPIRouter.register(pApp);
	}
}
