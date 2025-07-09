package com.mealam.showdown.context.battle;

public record MoveBattleContext(
		String moveId,
		String name,
		int currentPP,
		int maxPP) {}
