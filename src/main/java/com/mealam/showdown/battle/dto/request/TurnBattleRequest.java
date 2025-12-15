package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record TurnBattleRequest(
		@JsonProperty("turnNumber") Integer turnNumber,
		@JsonProperty("actions") @Nullable Map<String, String> actions
) {
}