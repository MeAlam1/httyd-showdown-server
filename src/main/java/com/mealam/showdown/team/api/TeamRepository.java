package com.mealam.showdown.team.api;

import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;

public interface TeamRepository {
	TeamContext save(TeamContext pTeam);

	TeamContext get(TeamId pTeamId);

	boolean delete(TeamId pTeamId);
}