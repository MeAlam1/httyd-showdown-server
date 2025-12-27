package com.mealam.showdown.team.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateTeamRequest(
		@JsonProperty("name") String name,
		@JsonProperty("dragonIds") List<String> dragonIds
) {
}