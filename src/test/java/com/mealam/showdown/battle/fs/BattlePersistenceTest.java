package com.mealam.showdown.battle.fs;

import com.mealam.showdown.battle.api.BattleBaseTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BattlePersistenceTest extends BattleBaseTest {
		
	private Path battleFile(String pBattleId) {
		String root = System.getProperty("showdown.data.dir");
		assertNotNull(root, "showdown.data.dir should be set by BattleBaseTest");
		return Path.of(root).resolve("battles").resolve(pBattleId + ".json");
	}

	@Test
	void createBattlePersistsToFile() throws Exception {
		String battleId = api.createBattleAndGetId();
		Path file = battleFile(battleId);

		assertTrue(Files.exists(file), "Battle file should exist after create");
		String content = Files.readString(file);
		assertFalse(content.isBlank());
		assertTrue(content.contains(battleId));
	}

	@Test
	void finishBattlePersistsToFile() throws Exception {
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
	void leavingAllPlayersPersistsToFile() throws Exception {
		String battleId = api.createBattleAndGetId();
		api.join(battleId, "p1", "team-p1");
		api.join(battleId, "p2", "team-p2");

		api.leave(battleId, "p1");
		api.leave(battleId, "p2");

		Path file = battleFile(battleId);
		assertTrue(Files.exists(file), "Battle file should exist when no players remain");
		String content = Files.readString(file);
		assertTrue(content.contains("\"teams\"") || content.contains("battleId"));
	}
}