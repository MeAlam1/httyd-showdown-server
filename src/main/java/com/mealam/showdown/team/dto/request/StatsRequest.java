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
    @JsonProperty("jawStrength") Integer jawStrength
) {}