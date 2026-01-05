/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.api;

import com.mealam.showdown.BaseTest;
import com.mealam.showdown.Main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;

public abstract class TeamBaseTest extends BaseTest {

	protected TeamApiClient api;

	@Override
	protected String loadResource(String pResourcePath) throws IOException {
		return super.loadResource("team/" + pResourcePath);
	}

	@BeforeEach
	void setupTeam() {
		try {
			Path classesRoot = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path dataRoot = classesRoot.resolve("..").normalize().resolve("httyd-showdown-server");
			Files.createDirectories(dataRoot.resolve("teams"));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to prepare teams dir", e);
		}

		api = new TeamApiClient(port, client);
	}
}
