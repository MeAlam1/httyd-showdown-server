/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.context;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.turns.TurnContext;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.types.ListUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record BattleContext(
		BattleId battleId,
		List<UserId> playerIds,
		List<UserId> spectatorIds,
		TurnContext turnContext,
		Phase phase,
		@Nullable UserId winnerPlayerId) {

	public BattleContext {
		playerIds = ListUtils.safeUnmodifiableList(playerIds);
		spectatorIds = ListUtils.safeUnmodifiableList(spectatorIds);
	}
}