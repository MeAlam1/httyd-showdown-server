/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
		@JsonProperty("trainingEffort") Map<String, Integer> trainingEffort) {}
