package com.mealam.showdown.team.context.dragon;

import java.util.Map;

public record NatureContext(
		String name,
		Map<String, Double> modifiers
) {
	public NatureContext {
		modifiers = modifiers == null ? Map.of() : Map.copyOf(modifiers);
	}
}