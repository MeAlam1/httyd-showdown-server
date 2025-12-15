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
		} catch (IOException pIoException) {
			throw new IllegalStateException("Failed to create battles directory: " + battlesDir, pIoException);
		}
	}

	@Override
	public BattleContext save(BattleContext pBattle) {
		write(toFilePath(pBattle.battleId()), pBattle);
		return pBattle;
	}

	@Override
	public BattleContext get(BattleId pBattleId) {
		Path file = toFilePath(pBattleId);
		if (!Files.exists(file)) return null;
		try {
			return mapper.readValue(file.toFile(), BattleContext.class);
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to read battle file: " + file, pIoException);
			return null;
		}
	}

	@Override
	public void update(BattleContext pBattle) {
		write(toFilePath(pBattle.battleId()), pBattle);
	}

	private Path toFilePath(BattleId pBattleId) {
		return battlesDir.resolve(pBattleId.toString() + ".json");
	}

	private void write(Path pFile, BattleContext pBattle) {
		try {
			Files.createDirectories(pFile.getParent());
			mapper.writeValue(pFile.toFile(), pBattle);
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to write battle file: " + pFile, pIoException);
			throw new IllegalStateException("Failed to persist battle: " + pBattle.battleId(), pIoException);
		}
	}
}