package com.mealam.showdown.battle.request;

import java.util.List;

public record CreateBattleRequest(
		List<String> playerIds) {}
