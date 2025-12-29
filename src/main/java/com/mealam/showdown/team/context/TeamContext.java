package com.mealam.showdown.team.context;

import com.mealam.showdown.team.data.TeamId;

import java.util.List;

public record TeamContext(
		TeamId teamId,
		String name,
		//List<String> dragonIds
		List<DragonContext> dragons
) {
	public TeamContext {
		/*dragonIds = dragonIds == null
				? List.of()
				: List.copyOf(dragonIds);*/
		dragons = dragons == null ? List.of() : List.copyOf(dragons);
	}
}