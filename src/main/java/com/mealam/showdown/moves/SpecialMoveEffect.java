package com.mealam.showdown.moves;

@FunctionalInterface
public interface SpecialMoveEffect {
	void apply(MoveContext pContext);
}
