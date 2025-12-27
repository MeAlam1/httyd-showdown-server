package com.mealam.showdown.team.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mealam.showdown.team.api.TeamRepository;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Stores each team as its own JSON file under a \`teams\` directory.
 *
 * <p>Path format:</p>
 * <pre>
 * {root}/teams/{teamId}.json
 * </pre>
 */
public class FileTeamRepository implements TeamRepository {

	private final Path teamsDir;
	private final ObjectMapper mapper;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public FileTeamRepository(Path pRoot) {
		if (pRoot == null) {
			throw new IllegalArgumentException("Root path must not be null");
		}

		this.teamsDir = pRoot.resolve("teams");
		this.mapper = new ObjectMapper()
				.findAndRegisterModules()
				.enable(SerializationFeature.INDENT_OUTPUT);

		BaseLogger.log(BaseLogLevel.INFO, "Initialized FileTeamRepository at: " + teamsDir);
	}

	@Override
	public TeamContext save(TeamContext pTeam) {
		Objects.requireNonNull(pTeam, "Team must not be null");
		Objects.requireNonNull(pTeam.teamId(), "teamId must not be null");

		lock.writeLock().lock();
		try {
			Files.createDirectories(teamsDir);

			Path target = teamFile(pTeam.teamId());
			Path tmp = target.resolveSibling(target.getFileName().toString() + ".tmp");

			mapper.writeValue(tmp.toFile(), pTeam);
			Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

			return pTeam;
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to save team file", pIoException);
			throw new IllegalStateException("Failed to persist team: " + pTeam.teamId(), pIoException);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public TeamContext get(TeamId pTeamId) {
		if (pTeamId == null) return null;

		lock.readLock().lock();
		try {
			Path file = teamFile(pTeamId);
			if (!Files.exists(file)) return null;

			return mapper.readValue(file.toFile(), TeamContext.class);
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to read team file for: " + pTeamId, pIoException);
			return null;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean delete(TeamId pTeamId) {
		if (pTeamId == null) return false;

		lock.writeLock().lock();
		try {
			Path file = teamFile(pTeamId);
			return Files.deleteIfExists(file);
		} catch (IOException pIoException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to delete team file for: " + pTeamId, pIoException);
			return false;
		} finally {
			lock.writeLock().unlock();
		}
	}

	private Path teamFile(TeamId pTeamId) {
		return teamsDir.resolve(pTeamId.toString() + ".json");
	}
}