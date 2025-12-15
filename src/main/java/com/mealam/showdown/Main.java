package com.mealam.showdown;

import com.mealam.showdown.loader.LoadInitializer;
import com.mealam.showdown.router.Routes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;

public final class Main {

	private Main() {
	}

	public static void main(String[] args) {
		Javalin app = createApp();
		Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
		app.start(7070);
	}

	public static Javalin createApp() {
		Javalin app = Javalin.create(config -> {
			config.bundledPlugins.enableCors(cors ->
					cors.addRule(rule -> rule.allowHost("http://localhost:7070"))
			);
			config.jsonMapper(new JavalinJackson());
		}).get("/", ctx -> ctx.result("Hello World"));

		LoadInitializer.init();
		
		Routes.register(app);

		app.exception(Exception.class, (e, ctx) ->
				ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(e.getMessage())
		);

		return app;
	}
}