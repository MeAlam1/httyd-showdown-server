package com.mealam.showdown.team.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mealam.showdown.data.BaseId;
import com.mealam.showdown.utils.IdGenerator;

public class TeamId extends BaseId {

	@JsonCreator
	public TeamId(String pValue) {
		super(pValue);
	}

	public static TeamId generate() {
		return new TeamId(IdGenerator.generateId());
	}

	public static TeamId parse(String pRaw) {
		return new TeamId(pRaw);
	}
}