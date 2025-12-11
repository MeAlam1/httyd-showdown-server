/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data.turns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnManager {

	public static final int NOT_STARTED = 0;
	public static final int FINISHED = -1;

	private final List<TurnContext> turnContexts = Collections.synchronizedList(new ArrayList<>());
	private volatile int currentTurn;
	private volatile TurnContext currentTurnContextInstance;

	public synchronized TurnContext createBattle() {
		currentTurn = NOT_STARTED;
		turnContexts.clear();
		return new TurnContext(NOT_STARTED, null);
	}

	public synchronized TurnContext startBattle() {
		if (currentTurn != NOT_STARTED) {
			throw new IllegalStateException("Battle already started or finished");
		}
		currentTurn = 1;
		return new TurnContext(currentTurn, null);
	}

	public synchronized TurnContext advance(TurnContext pTurnContextData) {
		if (currentTurn == FINISHED) {
			throw new IllegalStateException("Battle is over");
		}
		if (currentTurn == NOT_STARTED) {
			throw new IllegalStateException("Battle hasn't started");
		}
		turnContexts.add(pTurnContextData);
		currentTurn++;
		currentTurnContextInstance = pTurnContextData;
		return currentTurnContextInstance;
	}

	public synchronized void finish() {
		currentTurn = FINISHED;
		currentTurnContextInstance = new TurnContext(FINISHED, null);
	}

	public synchronized List<TurnContext> allTurns() {
		return List.copyOf(turnContexts);
	}
}
