package com.mealam.showdown.team.http;

import com.mealam.showdown.Main;
import com.mealam.showdown.team.api.TeamService;
import com.mealam.showdown.team.domain.DefaultTeamService;
import com.mealam.showdown.team.infra.DefaultTeamRepository;
import com.mealam.showdown.team.infra.FileTeamRepository;
import com.mealam.showdown.team.infra.InMemoryTeamRepository;
import io.javalin.Javalin;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TeamRouter {

	private final TeamController teamController;

	public TeamRouter(TeamService pService) {
		this.teamController = new TeamController(pService);
	}

	public static void configure(Javalin pApp) {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");

			InMemoryTeamRepository mem = new InMemoryTeamRepository();
			FileTeamRepository file = new FileTeamRepository(dataRoot);
			DefaultTeamRepository hybrid = new DefaultTeamRepository(mem, file);

			TeamService service = new DefaultTeamService(hybrid);

			new TeamRouter(service).register(pApp);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare data dir", e);
		}
	}

	public void register(Javalin pApp) {
		pApp.post("/api/team/create", teamController::createTeam);
		pApp.get("/api/team/{id}", teamController::getTeam);
		pApp.post("/api/team/{id}/delete", teamController::deleteTeam);
	}
}