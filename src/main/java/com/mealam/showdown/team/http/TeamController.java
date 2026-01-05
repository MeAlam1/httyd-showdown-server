/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.http;

import com.mealam.showdown.team.api.TeamService;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.team.dto.request.CreateTeamRequest;
import com.mealam.showdown.team.dto.response.TeamResponse;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

public class TeamController {

	private final TeamService service;

	public TeamController(TeamService pService) {
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
