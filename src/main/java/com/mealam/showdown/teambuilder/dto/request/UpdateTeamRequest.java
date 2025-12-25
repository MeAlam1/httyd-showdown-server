package com.mealam.showdown.teambuilder.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateTeamRequest(
		@JsonProperty("name") String name,
		@JsonProperty("dragonIds") List<String> dragonIds
) {
}