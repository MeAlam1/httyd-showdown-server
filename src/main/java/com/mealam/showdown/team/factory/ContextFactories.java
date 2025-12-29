package com.mealam.showdown.team.factory;

import com.mealam.showdown.team.context.DragonContext;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.context.dragon.*;

import com.mealam.showdown.team.data.TeamId;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ContextFactories {

	private ContextFactories() {
	}

	public static AbilityContext createAbility(String pId, String pName) {
		return new AbilityContext(pId, pName);
	}

	public static HeldItemContext createHeldItem(String pId, String pName, String pEffectDescription) {
		return new HeldItemContext(pId, pName, pEffectDescription);
	}

	public static LevelContext createLevel(int pLevel) {
		return new LevelContext(pLevel);
	}

	public static MoveContext createMove(String pMoveId, String pName, int pSlot) {
		return new MoveContext(pMoveId, pName, pSlot);
	}

	public static NatureContext createNature(String pName, Map<String, Double> pModifiers) {
		return new NatureContext(pName, pModifiers);
	}

	public static StatsContext createStats(
			int attack, int speed, int defense, int armor,
			int firepower, int stealth, int stamina,
			int shotLimit, int venom, int jawStrength) {
		return new StatsContext(
				attack, speed, defense, armor,
				firepower, stealth, stamina,
				shotLimit, venom, jawStrength
		);
	}

	public static TrainingEffortContext createTrainingEffort(Map<String, Integer> pValues) {
		return new TrainingEffortContext(pValues);
	}

	public static DragonContext createDragon(
			String pDragonId,
			String pNickname,
			NatureContext pNature,
			LevelContext pLevel,
			AbilityContext pAbility,
			StatsContext pStats,
			HeldItemContext pHeldItem,
			List<MoveContext> pMoves,
			TrainingEffortContext pTrainingEffort) {

		List<MoveContext> safeMoves = pMoves == null ? List.of() : List.copyOf(pMoves);
		return new DragonContext(
				pDragonId,
				pNickname,
				pNature,
				pLevel,
				pAbility,
				pStats,
				pHeldItem,
				safeMoves,
				pTrainingEffort
		);
	}

	public static TeamContext createTeam(TeamId pTeamId, String pName, List<DragonContext> pDragons) {
		List<DragonContext> safe = pDragons == null ? List.of() : List.copyOf(pDragons);
		return new TeamContext(Objects.requireNonNull(pTeamId, "teamId is required"), pName, safe);
	}
}