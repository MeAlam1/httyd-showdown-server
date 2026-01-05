/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.api;

import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.team.dto.request.CreateTeamRequest;

public interface TeamService {

	TeamContext createTeam(CreateTeamRequest pRequest);

	TeamContext getTeam(TeamId pTeamId);

	boolean deleteTeam(TeamId pTeamId);
}
