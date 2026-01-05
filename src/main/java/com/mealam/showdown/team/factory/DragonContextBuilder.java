/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.factory;

import com.mealam.showdown.team.context.DragonContext;
import com.mealam.showdown.team.context.dragon.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class DragonContextBuilder {

	private String dragonId;
	private String nickname;
	private NatureContext nature;
	private LevelContext level;
	private AbilityContext ability;
	private StatsContext stats;
	private HeldItemContext heldItem;
	private final List<MoveContext> moves = new ArrayList<>();
	private TrainingEffortContext trainingEffort;

	public static DragonContextBuilder builder() {
		return new DragonContextBuilder();
	}

	public DragonContextBuilder id(String pId) {
		this.dragonId = pId;
		return this;
	}

	public DragonContextBuilder nickname(String pName) {
		this.nickname = pName;
		return this;
	}

	public DragonContextBuilder nature(String pNature) {
		this.nature = ContextFactories.createNature(pNature);
		return this;
	}

	public DragonContextBuilder level(int pLevel) {
		this.level = ContextFactories.createLevel(pLevel);
		return this;
	}

	public DragonContextBuilder ability(String pId) {
		this.ability = ContextFactories.createAbility(pId);
		return this;
	}

	public DragonContextBuilder stats(
			int pAttack, int pSpeed, int pDefense, int pArmor,
			int pFirepower, int pStealth, int pStamina,
			int pShotLimit, int pVenom, int pJawStrength) {
		this.stats = ContextFactories.createStats(
				pAttack, pSpeed, pDefense, pArmor,
				pFirepower, pStealth, pStamina,
				pShotLimit, pVenom, pJawStrength);
		return this;
	}

	public DragonContextBuilder heldItem(String pId) {
		this.heldItem = ContextFactories.createHeldItem(pId);
		return this;
	}

	public DragonContextBuilder addMove(String pId, int pSlot) {
		this.moves.add(ContextFactories.createMove(pId, pSlot));
		return this;
	}

	public DragonContextBuilder trainingEffort(Map<String, Integer> pValues) {
		this.trainingEffort = ContextFactories.createTrainingEffort(pValues);
		return this;
	}

	public DragonContext build() {
		Objects.requireNonNull(dragonId, "dragonId is required");
		return ContextFactories.createDragon(
				dragonId,
				nickname,
				nature,
				level,
				ability,
				stats,
				heldItem,
				moves,
				trainingEffort);
	}
}
