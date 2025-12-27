package com.mealam.showdown.team.context;

import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.utils.types.ListUtils;

import java.util.ArrayList;
import java.util.List;

public record TeamContext(
		TeamId teamId,
		String name,
		List<String> dragonIds
) {
	public TeamContext {
		dragonIds = dragonIds == null ? List.of() : ListUtils.safeUnmodifiableList(new ArrayList<>(dragonIds));
	}
}