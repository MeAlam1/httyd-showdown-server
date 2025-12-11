package com.mealam.showdown;

import io.javalin.Javalin;

import java.io.InputStream;

public class Main {

	public static Javalin Main;

	public static void main(String[] pArgs) {
		Main = Javalin.create()
				.get("/", ctx -> ctx.result("Hello World"))
				.get("/static/api/{path...}", ctx -> {
					String path = ctx.pathParam("path");

					// basic sanitization
					if (path.contains("..") || path.startsWith("/")) {
						ctx.status(400).result("Invalid path");
						return;
					}

					// allow requests with or without the .json extension
					String resourcePath = "/static/api/" + (path.endsWith(".json") ? path : path + ".json");

					try (InputStream in = Main.class.getResourceAsStream(resourcePath)) {
						if (in == null) {
							ctx.status(404).result("Not Found");
							return;
						}

						String json = new String(in.readAllBytes());
						ctx.contentType("application/json");
						ctx.result(json);
					}
				})

				.start(7070);

	}
}