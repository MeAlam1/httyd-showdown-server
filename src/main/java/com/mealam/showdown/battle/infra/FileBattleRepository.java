/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mealam.showdown.battle.api.BattleRepository;
import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBattleRepository implements BattleRepository {

	private final Path battlesDir;
	private final ObjectMapper mapper;

	public FileBattleRepository(Path pRoot) {
		if (pRoot == null) {
			throw new IllegalArgumentException("Root path must not be null");
		}
		this.battlesDir = pRoot.resolve("battles");
		this.mapper = new ObjectMapper()
				.findAndRegisterModules()
				.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			Files.createDirectories(battlesDir);
			BaseLogger.log(BaseLogLevel.INFO, "Initialized FileBattleRepository at: " + battlesDir);
		} catch (IOException pIoException) {
			throw new IllegalStateException("Failed to create battles directory: " + battlesDir, pIoException);
		}
	}

	@Override
	public BattleContext save(BattleContext pBattle) {
		BaseLogger.log(BaseLogLevel.INFO, "FileRepo.save called for battleId=" + pBattle.battleId());
		write(toFilePath(pBattle.battleId()), pBattle);
		return pBattle;
	}

	@Override
	public BattleContext get(BattleId pBattleId) {
		Path file = toFilePath(pBattleId);
		if (!Files.exists(file)) {
			BaseLogger.log(BaseLogLevel.WARNING, "Battle file does not exist: " + file);
			return null;
		}
		try {
			BaseLogger.log(BaseLogLevel.INFO, "Reading battle file: " + file);
			return mapper.readValue(file.toFile(), BattleContext.class);
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to read battle file: " + file, pIoException);
			return null;
		}
	}

	@Override
	public void update(BattleContext pBattle) {
		BaseLogger.log(BaseLogLevel.INFO, "FileRepo.update called for battleId=" + pBattle.battleId());
		write(toFilePath(pBattle.battleId()), pBattle);
	}

	private Path toFilePath(BattleId pBattleId) {
		return battlesDir.resolve(pBattleId.toString() + ".json");
	}

	private void write(Path pFile, BattleContext pBattle) {
		try {
			Files.createDirectories(pFile.getParent());
			BaseLogger.log(BaseLogLevel.INFO, "Writing battle file: " + pFile);
			mapper.writeValue(pFile.toFile(), pBattle);
			BaseLogger.log(BaseLogLevel.INFO, "Battle persisted to file: " + pBattle.battleId());
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to write battle file: " + pFile, pIoException);
			throw new IllegalStateException("Failed to persist battle: " + pBattle.battleId(), pIoException);
		}
	}
}
