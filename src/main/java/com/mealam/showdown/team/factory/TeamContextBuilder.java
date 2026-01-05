/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.factory;

import com.mealam.showdown.team.context.DragonContext;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TeamContextBuilder {

	private TeamId teamId;
	private String name;
	private final List<DragonContext> dragons = new ArrayList<>();

	public static TeamContextBuilder builder() {
		return new TeamContextBuilder();
	}

	public TeamContextBuilder teamId(TeamId pId) {
		this.teamId = pId;
		return this;
	}

	public TeamContextBuilder name(String pName) {
		this.name = pName;
		return this;
	}

	public TeamContextBuilder addDragon(DragonContext pDragon) {
		if (pDragon != null) this.dragons.add(pDragon);
		return this;
	}

	public TeamContext build() {
		Objects.requireNonNull(teamId, "teamId is required");
		Objects.requireNonNull(name, "team name is required");
		return ContextFactories.createTeam(teamId, name, dragons);
	}
}
