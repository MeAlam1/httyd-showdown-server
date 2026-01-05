/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.fs;

import static org.junit.jupiter.api.Assertions.*;

import com.mealam.showdown.battle.api.BattleBaseTest;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class BattlePersistenceTest extends BattleBaseTest {

	private Path battleFile(String pBattleId) {
		String root = System.getProperty("showdown.data.dir");
		assertNotNull(root, "showdown.data.dir should be set by BattleBaseTest");
		return Path.of(root).resolve("battles").resolve(pBattleId + ".json");
	}

	@Test
	void shouldCreateBattleFile() throws Exception {
		String battleId = api.createBattleAndGetId();
		Path file = battleFile(battleId);

		assertTrue(Files.exists(file), "Battle file should exist after create");
		String content = Files.readString(file);
		assertFalse(content.isBlank());
		assertTrue(content.contains(battleId));
	}

	@Test
	void shouldFinishBattleStateToFile() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");
		api.start(battleId);
		api.finish(battleId, "p1");

		Path file = battleFile(battleId);
		assertTrue(Files.exists(file), "Battle file should exist after finish");
		String content = Files.readString(file);
		assertTrue(content.contains("\"winnerTeamId\"") || content.contains("winner"));
		assertTrue(content.contains("p1"));
	}

	@Test
	void shouldLeavePlayersStateToFile() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");

		api.leave(battleId, "p1");
		api.leave(battleId, "p2");

		Path file = battleFile(battleId);
		assertTrue(Files.exists(file), "Battle file should exist when no players remain");
		String content = Files.readString(file);
		// TODO: Improve check when player data is persisted, this is bullcrap
		assertTrue(content.contains("\"teams\"") || content.contains("battleId"));
	}
}
