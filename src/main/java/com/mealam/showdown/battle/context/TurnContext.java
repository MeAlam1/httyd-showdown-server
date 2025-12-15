package com.mealam.showdown.battle.context;

import com.mealam.showdown.user.data.UserId;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record TurnContext(
		int turnNumber,
		@Nullable Map<UserId, String> actions,
		@Nullable UserId activePlayerId
) {
}