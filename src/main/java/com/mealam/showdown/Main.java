/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {

	public static void main(String[] pArgs) {
		SpringApplication.run(Main.class, pArgs);
	}

	@RestController
	static class TestKotlin {

		@GetMapping("/test-kt")
		public String test() {
			return Test.greet("Kotlin");
		}
	}
}
