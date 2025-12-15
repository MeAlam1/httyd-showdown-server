package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LeaveBattleRequest(@JsonProperty("userId") String userId) {
}