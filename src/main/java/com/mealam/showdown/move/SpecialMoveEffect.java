package com.mealam.showdown.move;

import com.mealam.showdown.context.MoveContext;

@FunctionalInterface
public interface SpecialMoveEffect {
	void apply(MoveContext pContext);
}
