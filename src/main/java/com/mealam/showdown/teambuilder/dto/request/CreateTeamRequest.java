package com.mealam.showdown.teambuilder.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateTeamRequest(
		@JsonProperty("ownerId") String ownerId,
		@JsonProperty("name") String name,
		@JsonProperty("dragonIds") List<String> dragonIds
) {
}