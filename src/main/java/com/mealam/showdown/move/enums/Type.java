package com.mealam.showdown.move.enums;

public enum Type {

	GENERAL,
	BOULDER,
	MYSTERY,
	SHARP,
	STOKER,
	STRIKE,
	TIDAL,
	TRACKER;

	public static Type fromString(String pType) {
		try {
			return Type.valueOf(pType.toLowerCase());
		} catch (IllegalArgumentException pException) {
			throw new IllegalArgumentException("Unknown category: " + pType);
		}
	}
}
