package com.mealam.showdown.team.context.dragon;

public record MoveContext(
		String moveId,
		int slot
) {
	public MoveContext {
		if (slot < 1) slot = 1;
		if (slot > 4) slot = 4;
	}
}