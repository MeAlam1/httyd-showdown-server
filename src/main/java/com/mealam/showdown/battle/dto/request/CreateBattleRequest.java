package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateBattleRequest(@JsonProperty("playerIds") List<String> playerIds,
                                  @JsonProperty("spectatorIds") List<String> spectatorIds,
                                  @JsonProperty("phase") String phase) {
}