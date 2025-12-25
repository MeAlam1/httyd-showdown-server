package com.mealam.showdown.teambuilder.http;

import com.mealam.showdown.teambuilder.api.TeamBuilderService;
import io.javalin.Javalin;

public class TeamBuilderRouter {

	private final TeamBuilderController controller;

	public TeamBuilderRouter(TeamBuilderService pService) {
		this.controller = new TeamBuilderController(pService);
	}

	public static void configure(Javalin pApp, TeamBuilderService pService) {
		new TeamBuilderRouter(pService).register(pApp);
	}

	public void register(Javalin pApp) {
		pApp.post("/api/team-builder/team", controller::createTeam);
		pApp.get("/api/team-builder/team/{id}", controller::getTeam);
		pApp.get("/api/team-builder/teams", controller::listTeams);
		pApp.put("/api/team-builder/team/{id}", controller::updateTeam);
		pApp.delete("/api/team-builder/team/{id}", controller::deleteTeam);
	}
}