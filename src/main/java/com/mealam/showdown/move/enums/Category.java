package com.mealam.showdown.move.enums;

public enum Category {

	PHYSICAL,
	RANGED,
	STATUS;

	public static Category fromString(String pCategory) {
		try {
			return Category.valueOf(pCategory.toLowerCase());
		} catch (IllegalArgumentException pException) {
			throw new IllegalArgumentException("Unknown category: " + pCategory);
		}
	}
}
