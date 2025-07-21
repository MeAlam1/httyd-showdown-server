package com.mealam.showdown.battle.context;

public record MoveBattleContext(
		String moveId,
		String name,
		int currentPP,
		int maxPP) {}
