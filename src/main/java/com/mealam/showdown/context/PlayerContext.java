package com.mealam.showdown.context;

import com.mealam.showdown.context.user.UserContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PlayerContext(
		String playerId,
		@Nullable UserContext user,
		List<String> friendIds,
		List<DragonContext> party,
		int totalWins,
		int totalLosses,
		long lastActiveTimestamp,
		String avatarUrl,
		List<String> achievements
) {
}