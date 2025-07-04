package com.mealam.showdown.moves.enums;

public enum Status {

	FEAR,
	BURN,
	POISON,
	SLEEP,
	NOSLEEP,
	CONFUSION,
	COPY,
	STUNNED,
	HEAL,
	SHOCKED,
	HARM,
	PROTECT,
	MULTIPLY,
	PARALYSIS;

	public static Status fromString(String pStatus) {
		try {
			return Status.valueOf(pStatus.toLowerCase());
		} catch (IllegalArgumentException pException) {
			throw new IllegalArgumentException("Unknown category: " + pStatus);
		}
	}
}
