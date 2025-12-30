package com.mealam.showdown.team.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record CreateDragonRequest(
    @JsonProperty("id") String id,
    @JsonProperty("nickname") String nickname,
    @JsonProperty("natureId") String natureId,
    @JsonProperty("level") Integer level,
    @JsonProperty("abilityId") String abilityId,
    @JsonProperty("stats") StatsRequest stats,
    @JsonProperty("heldItemId") String heldItemId,
    @JsonProperty("moves") List<MoveRequest> moves,
    @JsonProperty("trainingEffort") Map<String, Integer> trainingEffort
) {}