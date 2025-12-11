/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown;

import io.javalin.Javalin;

import java.io.InputStream;

public class Main {

	public static Javalin Main;

	public static void main(String[] pArgs) {

		Main = Javalin.create(/*config*/)
				.get("/", ctx -> ctx.result("Hello World"))
				.get("static/api/dragons/dob/{name}", ctx -> {
					String name = ctx.pathParam("name");
					String resourcepath = "/static/api/dragons/dob/" + name + ".json";

					try (InputStream in = com.mealam.showdown.Main.class.getResourceAsStream(resourcepath)) {
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
