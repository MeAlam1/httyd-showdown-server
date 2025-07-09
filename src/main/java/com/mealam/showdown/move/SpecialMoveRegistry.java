package com.mealam.showdown.move;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpecialMoveRegistry {

	private static final Map<String, SpecialMoveEffect> registry = new ConcurrentHashMap<>();

	public static void register(String pMoveId, SpecialMoveEffect pEffect) {
		registry.put(pMoveId, pEffect);
	}

	public static SpecialMoveEffect get(String pMoveId) {
		return registry.get(pMoveId);
	}
}
