package com.mealam.showdown.battle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record TurnBattleResponse(
		@JsonProperty("turnNumber") int turnNumber,
		@JsonProperty("actions") @Nullable Map<String, String> actions
) {
}
