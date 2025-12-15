package com.mealam.showdown.battle.context;

import com.mealam.showdown.user.data.UserId;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record TurnHistoryContext(
		int turnNumber,
		@Nullable Map<UserId, String> actions
) {
}