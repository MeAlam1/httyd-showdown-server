/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CreateBattleRequest(@JsonProperty("playerIds") List<String> playerIds,
		@JsonProperty("spectatorIds") List<String> spectatorIds,
		@JsonProperty("phase") String phase) {}
