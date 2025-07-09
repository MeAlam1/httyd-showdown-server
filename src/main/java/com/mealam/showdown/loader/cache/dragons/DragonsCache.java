package com.mealam.showdown.loader.cache.dragons;

import java.util.List;

public record DragonsCache(
		String id,
		String name,
		String img,
		String origin,
		List<String> classes,
		int attack,
		int speed,
		int armor,
		int firepower,
		int shot_limit,
		int venom,
		int jaw_strength,
		int stealth) {}
