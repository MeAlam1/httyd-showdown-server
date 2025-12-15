package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.TeamId;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.types.ListUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record BattleContext(
		BattleId battleId,
		Map<TeamId, List<UserId>> teams,
		List<UserId> spectatorIds,
		TurnContext turnContext,
		Phase phase,
		@Nullable TeamId winnerTeamId,
		List<TurnHistoryContext> turnHistory
) {

	public BattleContext {
		Map<TeamId, List<UserId>> mutableTeams = new LinkedHashMap<>();
		if (teams != null) {
			teams.forEach((teamId, playerIds) ->
					mutableTeams.put(teamId, ListUtils.safeUnmodifiableList(playerIds))
			);
		}
		teams = Collections.unmodifiableMap(mutableTeams);
		spectatorIds = ListUtils.safeUnmodifiableList(spectatorIds);
		turnHistory = turnHistory == null ? List.of() : ListUtils.safeUnmodifiableList(new ArrayList<>(turnHistory));
	}

	public List<UserId> getAllPlayerIds() {
		List<UserId> all = new ArrayList<>();
		if (teams != null) {
			teams.values().forEach(all::addAll);
		}
		return all;
	}

	@Nullable
	public TeamId getTeamForPlayer(UserId pPlayerId) {
		if (teams == null) return null;
		for (Map.Entry<TeamId, List<UserId>> entry : teams.entrySet()) {
			if (entry.getValue().contains(pPlayerId)) {
				return entry.getKey();
			}
		}
		return null;
	}
}