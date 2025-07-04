package com.mealam.showdown.battle;

public record MoveBattleContext(
		String moveId,
		String name,
		int currentPP,
		int maxPP
) {
}