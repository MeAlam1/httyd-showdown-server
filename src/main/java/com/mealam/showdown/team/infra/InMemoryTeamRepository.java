package com.mealam.showdown.team.infra;

import com.mealam.showdown.team.api.TeamRepository;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTeamRepository implements TeamRepository {

	private final Map<TeamId, TeamContext> teams = new ConcurrentHashMap<>();

	@Override
	public TeamContext save(TeamContext pTeam) {
		teams.put(pTeam.teamId(), pTeam);
		return pTeam;
	}

	@Override
	public TeamContext get(TeamId pTeamId) {
		return teams.get(pTeamId);
	}

	@Override
	public boolean delete(TeamId pTeamId) {
		return teams.remove(pTeamId) != null;
	}
}