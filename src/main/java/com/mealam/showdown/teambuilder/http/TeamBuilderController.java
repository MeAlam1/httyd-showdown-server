package com.mealam.showdown.teambuilder.http;

import com.mealam.showdown.teambuilder.api.TeamBuilderService;
import com.mealam.showdown.teambuilder.data.TeamId;
import com.mealam.showdown.teambuilder.dto.request.CreateTeamRequest;
import com.mealam.showdown.teambuilder.dto.request.UpdateTeamRequest;
import com.mealam.showdown.teambuilder.dto.response.TeamResponse;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

public class TeamBuilderController {

	private final TeamBuilderService service;

	public TeamBuilderController(TeamBuilderService pService) {
		this.service = pService;
	}

	public void createTeam(Context pContext) {
		try {
			var req = pContext.bodyAsClass(CreateTeamRequest.class);
			var team = service.createTeam(req);
			ResponseUtils.created(pContext, new TeamResponse(team));
		} catch (IllegalArgumentException ex) {
			ResponseUtils.badRequest(pContext, ex.getMessage(), "team_create_invalid");
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error creating team", ex);
			ResponseUtils.serverError(pContext, "Failed to create team", "team_create_failed");
		}
	}

	public void getTeam(Context pContext) {
		try {
			var id = TeamId.parse(pContext.pathParam("id"));
			var team = service.getTeam(id);
			if (team == null) {
				ResponseUtils.notFound(pContext, "Team not found", "team_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new TeamResponse(team));
		} catch (IllegalArgumentException ex) {
			ResponseUtils.badRequest(pContext, "Invalid team ID", "invalid_team_id");
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving team", ex);
			ResponseUtils.serverError(pContext, "Internal server error", "team_get_error");
		}
	}

	public void listTeams(Context pContext) {
		try {
			var ownerId = pContext.queryParam("ownerId");
			var teams = service.listTeams(ownerId);
			ResponseUtils.ok(pContext, teams);
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error listing teams", ex);
			ResponseUtils.serverError(pContext, "Internal server error", "team_list_error");
		}
	}

	public void updateTeam(Context pContext) {
		try {
			var id = TeamId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(UpdateTeamRequest.class);
			var team = service.updateTeam(id, req);
			if (team == null) {
				ResponseUtils.notFound(pContext, "Team not found", "team_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new TeamResponse(team));
		} catch (IllegalArgumentException ex) {
			ResponseUtils.badRequest(pContext, ex.getMessage(), "team_update_invalid");
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error updating team", ex);
			ResponseUtils.serverError(pContext, "Internal server error", "team_update_error");
		}
	}

	public void deleteTeam(Context pContext) {
		try {
			var id = TeamId.parse(pContext.pathParam("id"));
			boolean deleted = service.deleteTeam(id);
			if (!deleted) {
				ResponseUtils.notFound(pContext, "Team not found", "team_not_found");
				return;
			}
			ResponseUtils.ok(pContext, java.util.Map.of("deleted", true));
		} catch (IllegalArgumentException ex) {
			ResponseUtils.badRequest(pContext, "Invalid team ID", "invalid_team_id");
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error deleting team", ex);
			ResponseUtils.serverError(pContext, "Internal server error", "team_delete_error");
		}
	}
}