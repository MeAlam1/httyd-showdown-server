package com.mealam.showdown.team.domain;

import com.mealam.showdown.team.api.TeamRepository;
import com.mealam.showdown.team.api.TeamService;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.team.dto.request.CreateTeamRequest;

import java.util.Objects;

public class DefaultTeamService implements TeamService {

	private final TeamRepository repo;

	public DefaultTeamService(TeamRepository pRepo) {
		this.repo = Objects.requireNonNull(pRepo, "PartyRepository is required");
	}

	@Override
	public TeamContext createTeam(CreateTeamRequest pRequest) {
		if (pRequest == null) throw new IllegalArgumentException("Request is required");
		if (pRequest.name() == null || pRequest.name().isBlank())
			throw new IllegalArgumentException("name is required");

		TeamId id = TeamId.generate();
		var ctx = new TeamContext(id, pRequest.name(), pRequest.dragonIds());
		return repo.save(ctx);
	}

	@Override
	public TeamContext getTeam(TeamId pTeamId) {
		return repo.get(pTeamId);
	}

	@Override
	public boolean deleteTeam(TeamId pTeamId) {
		return repo.delete(pTeamId);
	}
}