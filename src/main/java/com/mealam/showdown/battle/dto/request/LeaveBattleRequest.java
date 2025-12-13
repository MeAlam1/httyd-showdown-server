package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LeaveBattleRequest(String userId) {

	@JsonCreator
	public LeaveBattleRequest(
			@JsonProperty("userId") String userId) {
		this.userId = userId;
	}
}