package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateBattleRequest(List<String> playerIds, List<String> spectatorIds, String phase) {

	@JsonCreator
	public CreateBattleRequest(
			@JsonProperty("playerIds") List<String> playerIds,
			@JsonProperty("spectatorIds") List<String> spectatorIds,
			@JsonProperty("phase") String phase) {
		this.playerIds = playerIds;
		this.spectatorIds = spectatorIds;
		this.phase = phase;
	}
}