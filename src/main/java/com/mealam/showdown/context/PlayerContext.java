package com.mealam.showdown.context;

import com.mealam.showdown.user.context.UserContext;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record PlayerContext(
		String playerId,
		@Nullable UserContext user,
		List<String> friendIds,
		List<DragonContext> party,
		int totalWins,
		int totalLosses,
		long lastActiveTimestamp,
		String avatarUrl,
		List<String> achievements) {}
