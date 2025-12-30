package com.mealam.showdown.team.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MoveRequest(
    @JsonProperty("moveId") String moveId,
    @JsonProperty("slot") Integer slot
) {}