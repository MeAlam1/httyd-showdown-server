package com.mealam.showdown.players;

import com.mealam.showdown.dragons.DragonContext;
import com.mealam.showdown.users.UserContext;
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