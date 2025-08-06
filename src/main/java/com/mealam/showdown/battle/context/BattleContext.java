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
import com.mealam.showdown.battle.data.turns.Turn;
import com.mealam.showdown.user.data.UserId;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record BattleContext(
		BattleId battleId,
		List<UserId> playerIds,
		List<UserId> spectatorIds,
		Turn turn,
		Phase phase,
		@Nullable UserId winnerPlayerId) {

	public BattleContext {
		playerIds = Collections.unmodifiableList(playerIds);
		spectatorIds = Collections.unmodifiableList(spectatorIds);
	}
}
