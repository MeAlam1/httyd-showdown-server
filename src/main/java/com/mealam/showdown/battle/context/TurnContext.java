package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.TeamId;
import com.mealam.showdown.user.data.UserId;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public record TurnContext(
		int turnNumber,
		@Nullable Map<UserId, String> actions,
		@Nullable TeamId activeTeamId,
		@Nullable Set<UserId> playersWhoActed
) {
}