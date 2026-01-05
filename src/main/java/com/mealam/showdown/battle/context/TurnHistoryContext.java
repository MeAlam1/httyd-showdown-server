/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.TeamId;
import com.mealam.showdown.user.data.UserId;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable replay record for a single turn.
 * Reading these records in order should be sufficient to recreate the battle timeline.
 */
public record TurnHistoryContext(
		int turnNumber,
		Instant startedAt,
		@Nullable Instant endedAt,
		@Nullable TeamId openingTeamId,
		List<TurnEvent> events,
		@Nullable TurnResolution resolution) {

	public sealed interface TurnEvent permits
			TurnEvent.ActionSubmitted,
			TurnEvent.TeamCompleted,
			TurnEvent.TurnAdvanced,
			TurnEvent.BattleFinished {

		Instant at();

		record ActionSubmitted(
				Instant at,
				UserId userId,
				TeamId teamId,
				String action) implements TurnEvent {}

		record TeamCompleted(
				Instant at,
				TeamId teamId) implements TurnEvent {}

		record TurnAdvanced(
				Instant at,
				int fromTurnNumber,
				int toTurnNumber,
				@Nullable TeamId nextOpeningTeamId) implements TurnEvent {}

		record BattleFinished(
				Instant at,
				@Nullable TeamId winnerTeamId) implements TurnEvent {}
	}

	public record TurnResolution(
			/**
			 * Final actions for this turn, keyed by player id as String for JSON friendliness.
			 */
			Map<String, String> finalActions) {}
}
