/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.dto.response;

import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.context.SpectatorBattleContext;

public sealed interface JoinBattleResponse permits JoinBattleResponse.Player, JoinBattleResponse.Spectator {

	record Player(PlayerBattleContext playerBattleContext) implements JoinBattleResponse {}

	record Spectator(SpectatorBattleContext spectatorBattleContext) implements JoinBattleResponse {}
}
