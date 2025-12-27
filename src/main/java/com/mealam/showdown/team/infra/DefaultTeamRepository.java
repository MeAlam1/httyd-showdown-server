package com.mealam.showdown.team.infra;

import com.mealam.showdown.team.api.TeamRepository;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;

import java.util.Objects;

public class DefaultTeamRepository implements TeamRepository {

	private final InMemoryTeamRepository memRepo;
	private final FileTeamRepository fileRepo;

	public DefaultTeamRepository(InMemoryTeamRepository pMemRepo, FileTeamRepository pFileRepo) {
		this.memRepo = Objects.requireNonNull(pMemRepo);
		this.fileRepo = Objects.requireNonNull(pFileRepo);
		BaseLogger.log(BaseLogLevel.INFO, "HybridTeamRepository initialized");
	}

	@Override
	public TeamContext save(TeamContext pTeam) {
		BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.save -> memory for teamId=" + pTeam.teamId());
		memRepo.save(pTeam);

		BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.save -> writing to file for teamId=" + pTeam.teamId());
		fileRepo.save(pTeam);

		return pTeam;
	}

	@Override
	public TeamContext get(TeamId pTeamId) {
		TeamContext ctx = memRepo.get(pTeamId);
		if (ctx != null) {
			BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.get -> found in memory for teamId=" + pTeamId);
			return ctx;
		}

		BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.get -> not in memory, checking file for teamId=" + pTeamId);
		ctx = fileRepo.get(pTeamId);
		if (ctx != null) {
			BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.get -> loaded from file, caching in memory for teamId=" + pTeamId);
			memRepo.save(ctx);
		} else {
			BaseLogger.log(BaseLogLevel.WARNING, "HybridTeam.get -> team not found anywhere for teamId=" + pTeamId);
		}

		return ctx;
	}

	@Override
	public boolean delete(TeamId pTeamId) {
		BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.delete -> removing from memory for teamId=" + pTeamId);
		boolean mem = memRepo.delete(pTeamId);

		BaseLogger.log(BaseLogLevel.INFO, "HybridTeam.delete -> deleting from file for teamId=" + pTeamId);
		boolean file = fileRepo.delete(pTeamId);

		return mem || file;
	}
}