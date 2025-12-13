package com.mealam.showdown.battle.context;

public record TurnContext(int turnNumber) {
	public TurnContext() {
		this(0);
	}
}