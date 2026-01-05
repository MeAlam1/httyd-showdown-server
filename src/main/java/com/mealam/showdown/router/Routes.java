/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.router;

import com.mealam.showdown.api.StaticAPIRouter;
import com.mealam.showdown.battle.http.BattleRouter;
import com.mealam.showdown.team.http.TeamRouter;
import io.javalin.Javalin;

public class Routes {

	public static void register(Javalin pApp) {
		StaticAPIRouter.register(pApp);
		BattleRouter.configure(pApp);
		TeamRouter.configure(pApp);
	}
}
