/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data;

import com.mealam.showdown.battle.context.TurnContext;
import java.util.Map;

public class TurnManager {

	public static final int NOT_STARTED = 0;
	public static final int FINISHED = -1;

	private volatile int currentTurn;
	private volatile TurnContext currentTurnContextInstance;

	public synchronized TurnContext createBattle() {
		currentTurn = NOT_STARTED;
		currentTurnContextInstance = new TurnContext(
				NOT_STARTED,
				TurnContext.TurnStatus.NOT_STARTED,
				null,
				null);
		return currentTurnContextInstance;
	}

	public synchronized TurnContext startBattle(TeamId pStartingTeamId) {
		if (currentTurnContextInstance != null && currentTurnContextInstance.status() != TurnContext.TurnStatus.NOT_STARTED) {
			throw new IllegalStateException("Battle already started or finished");
		}
		currentTurn = 1;
		currentTurnContextInstance = new TurnContext(
				currentTurn,
				TurnContext.TurnStatus.IN_PROGRESS,
				pStartingTeamId,
				Map.of());
		return currentTurnContextInstance;
	}

	public synchronized TurnContext advance(TeamId pNextActiveTeamId) {
		if (currentTurnContextInstance != null && currentTurnContextInstance.status() == TurnContext.TurnStatus.FINISHED) {
			throw new IllegalStateException("Battle is over");
		}
		if (currentTurnContextInstance == null || currentTurnContextInstance.status() == TurnContext.TurnStatus.NOT_STARTED) {
			throw new IllegalStateException("Battle hasn't started");
		}

		currentTurn = Math.max(1, currentTurn + 1);
		currentTurnContextInstance = new TurnContext(
				currentTurn,
				TurnContext.TurnStatus.IN_PROGRESS,
				pNextActiveTeamId,
				Map.of());
		return currentTurnContextInstance;
	}

	public synchronized TurnContext finish() {
		currentTurn = FINISHED;
		currentTurnContextInstance = new TurnContext(
				FINISHED,
				TurnContext.TurnStatus.FINISHED,
				null,
				null);
		return currentTurnContextInstance;
	}
}
