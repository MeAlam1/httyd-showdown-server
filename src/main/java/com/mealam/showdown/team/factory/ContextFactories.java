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

	public static AbilityContext createAbility(String pId) {
		return new AbilityContext(pId);
	}

	public static HeldItemContext createHeldItem(String pId) {
		return new HeldItemContext(pId);
	}

	public static LevelContext createLevel(int pLevel) {
		return new LevelContext(pLevel);
	}

	public static MoveContext createMove(String pId, int pSlot) {
		return new MoveContext(pId, pSlot);
	}

	public static NatureContext createNature(String pId) {
		return new NatureContext(pId);
	}

	public static StatsContext createStats(
			int pAttack, int pSpeed, int pDefense, int pArmor,
			int pFirepower, int pStealth, int pStamina,
			int pShotLimit, int pVenom, int pJawStrength) {
		return new StatsContext(
				pAttack, pSpeed, pDefense, pArmor,
				pFirepower, pStealth, pStamina,
				pShotLimit, pVenom, pJawStrength);
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