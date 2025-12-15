package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.types.ListUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record BattleContext(
		BattleId battleId,
		List<UserId> playerIds,
		List<UserId> spectatorIds,
		TurnContext turnContext,
		Phase phase,
		@Nullable UserId winnerPlayerId,
		List<TurnContext> turnHistory
) {

	public BattleContext {
		playerIds = ListUtils.safeUnmodifiableList(playerIds);
		spectatorIds = ListUtils.safeUnmodifiableList(spectatorIds);
		turnHistory = turnHistory == null ? List.of() : ListUtils.safeUnmodifiableList(new ArrayList<>(turnHistory));
	}
}