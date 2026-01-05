/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.party;

import com.mealam.showdown.battle.context.DragonBattleContext;
import com.mealam.showdown.battle.context.MoveBattleContext;
import com.mealam.showdown.move.enums.Status;
import com.mealam.showdown.user.data.UserId;
import java.util.ArrayList;
import java.util.List;

public class PartyService {

	private static final int MAX_PARTY_SIZE = 6;

	public List<DragonBattleContext> getUserParty(UserId pUserId) {
		// TODO: Placeholder data - replace with actual database fetching
		List<DragonBattleContext> party = new ArrayList<>();

		party.add(new DragonBattleContext(
				"deadly_nadder",
				"Deadly Nadder",
				15,
				85,
				100,
				Status.NONE,
				List.of(
						new MoveBattleContext("ember", "Ember", 25, 25),
						new MoveBattleContext("scratch", "Scratch", 35, 35),
						new MoveBattleContext("growl", "Growl", 40, 40)),
				true));

		party.add(new DragonBattleContext(
				"flightmare",
				"Flightmare",
				12,
				70,
				80,
				Status.NONE,
				List.of(
						new MoveBattleContext("water_gun", "Water Gun", 25, 25),
						new MoveBattleContext("tackle", "Tackle", 35, 35)),
				false));

		party.add(new DragonBattleContext(
				"thunderclaw",
				"Thunderclaw",
				10,
				60,
				65,
				Status.NONE,
				List.of(
						new MoveBattleContext("thunder_shock", "Thunder Shock", 30, 30),
						new MoveBattleContext("quick_attack", "Quick Attack", 30, 30)),
				false));

		return party;
	}

	public boolean isValidParty(List<DragonBattleContext> pParty) {
		if (pParty == null || pParty.isEmpty()) {
			return false;
		}

		if (pParty.size() > MAX_PARTY_SIZE) {
			return false;
		}

		boolean hasActiveDragon = pParty.stream().anyMatch(DragonBattleContext::isActive);
		if (!hasActiveDragon) {
			return false;
		}

		return pParty.stream().allMatch(dragon -> dragon.currentHp() > 0 &&
				dragon.moves() != null &&
				!dragon.moves().isEmpty());
	}

	public List<DragonBattleContext> preparePartyForBattle(List<DragonBattleContext> pParty) {
		if (!isValidParty(pParty)) {
			return new ArrayList<>();
		}
		if (pParty == null || pParty.isEmpty()) {
			return new ArrayList<>();
		}

		List<DragonBattleContext> prepared = new ArrayList<>();
		for (int i = 0; i < pParty.size(); i++) {
			DragonBattleContext dragon = pParty.get(i);
			prepared.add(new DragonBattleContext(
					dragon.dragonId(),
					dragon.name(),
					dragon.level(),
					dragon.currentHp(),
					dragon.maxHp(),
					dragon.status(),
					dragon.moves(),
					i == 0));
		}

		return prepared;
	}
}
