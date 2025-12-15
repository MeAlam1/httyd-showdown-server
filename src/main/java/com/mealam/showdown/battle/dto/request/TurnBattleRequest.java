package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public record TurnBattleRequest(
		@JsonProperty("action") @Nullable String action
) {
}