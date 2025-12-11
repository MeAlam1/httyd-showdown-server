package com.mealam.showdown;

import com.mealam.showdown.config.Routes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.InputStream;

public class Main {

	public static Javalin app;

	public static void main(String[] pArgs) {
		app = Javalin.create(config -> {
					config.bundledPlugins.enableCors(cors -> {
						cors.addRule(rule -> rule.allowHost("http://localhost:7070"));
					});
				})
				.get("/", ctx -> ctx.result("Hello World"));

		Routes.register(app);

		app.exception(Exception.class, (e, ctx) -> {
			ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(e.getMessage());
		});


		app.start(7070);
	}
}