/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.TeamId;
import java.time.Instant;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * Current in-memory snapshot for the ongoing turn.
 * This is "state", not a replay log.
 */
public record TurnContext(
		int turnNumber,
		TurnStatus status,
		@Nullable TeamId activeTeamId,

		/**
		 * Current per-turn submissions so far (draft state).
		 * Keyed by acting player (as String) for JSON friendliness.
		 */
		@Nullable Map<String, PlayerTurnSubmission> submissions) {

	public enum TurnStatus {
		NOT_STARTED,
		IN_PROGRESS,
		FINISHED
	}

	public record PlayerTurnSubmission(
			String userId,
			String action,
			Instant submittedAt) {}
}
