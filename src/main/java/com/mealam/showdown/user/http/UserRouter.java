package com.mealam.showdown.user.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealam.showdown.Main;
import com.mealam.showdown.user.domain.UserService;
import com.mealam.showdown.user.infra.CachedUserRepository;
import com.mealam.showdown.user.infra.FileUserRepository;
import io.javalin.Javalin;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class UserRouter {

	private final UserController controller;

	public UserRouter(UserService pService) {
		this.controller = new UserController(pService);
	}

	public static void configure(Javalin pApp) {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");
			Path usersFile = dataRoot.resolve("users.json");

			ObjectMapper mapper = new ObjectMapper();

			var fileRepo = new FileUserRepository(usersFile, mapper);
			var cachedRepo = new CachedUserRepository(fileRepo);

			UserService service = new UserService(cachedRepo);

			new UserRouter(service).register(pApp);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare user system", e);
		}
	}

	public void register(Javalin pApp) {
		pApp.post("/api/user", controller::createUser);
		pApp.get("/api/user/{id}", controller::getProfile);
	}
}