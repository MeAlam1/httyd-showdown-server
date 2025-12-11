/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown;


import com.mealam.showdown.loader.LoadInitializer;
import io.javalin.Javalin;

public class Main {

	public static Javalin Main;
	
	public static void main(String[] pArgs) {
		LoadInitializer.init();

		Main = Javalin.create(/*config*/)
				.get("/", ctx -> ctx.result("Hello World"))
				.start(7070);
	}
}
