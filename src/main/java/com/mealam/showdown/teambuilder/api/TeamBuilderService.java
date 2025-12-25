package com.mealam.showdown.teambuilder.api;

import com.mealam.showdown.teambuilder.context.TeamContext;
import com.mealam.showdown.teambuilder.data.TeamId;
import com.mealam.showdown.teambuilder.dto.request.CreateTeamRequest;
import com.mealam.showdown.teambuilder.dto.request.UpdateTeamRequest;

import java.util.List;

public interface TeamBuilderService {
	TeamContext createTeam(CreateTeamRequest pRequest);

	TeamContext getTeam(TeamId pTeamId);

	List<TeamContext> listTeams(String pOwnerId);

	TeamContext updateTeam(TeamId pTeamId, UpdateTeamRequest pRequest);

	boolean deleteTeam(TeamId pTeamId);
}