/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatsRequest(
		@JsonProperty("attack") Integer attack,
		@JsonProperty("speed") Integer speed,
		@JsonProperty("defense") Integer defense,
		@JsonProperty("armor") Integer armor,
		@JsonProperty("firepower") Integer firepower,
		@JsonProperty("stealth") Integer stealth,
		@JsonProperty("stamina") Integer stamina,
		@JsonProperty("shotLimit") Integer shotLimit,
		@JsonProperty("venom") Integer venom,
		@JsonProperty("jawStrength") Integer jawStrength) {}
