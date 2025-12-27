package com.mealam.showdown.team.api;

import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.team.dto.request.CreateTeamRequest;

public interface TeamService {
	TeamContext createTeam(CreateTeamRequest pRequest);

	TeamContext getTeam(TeamId pTeamId);

	boolean deleteTeam(TeamId pTeamId);
}