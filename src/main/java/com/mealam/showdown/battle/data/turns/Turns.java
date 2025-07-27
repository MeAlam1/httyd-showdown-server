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

public class Turns {

	public static final int NOT_STARTED = 0;
	public static final int FINISHED = -1;

	private final List<Turn> turns = Collections.synchronizedList(new ArrayList<>());
	private volatile int currentTurn;
	private volatile Turn currentTurnInstance;

	public synchronized Turn createBattle() {
		currentTurn = NOT_STARTED;
		turns.clear();
		return new Turn(NOT_STARTED, null);
	}

	public synchronized Turn startBattle() {
		if (currentTurn != NOT_STARTED) {
			throw new IllegalStateException("Battle already started or finished");
		}
		currentTurn = 1;
		return new Turn(currentTurn, null);
	}

	public synchronized Turn advance(Turn pTurnData) {
		if (currentTurn == FINISHED) {
			throw new IllegalStateException("Battle is over");
		}
		if (currentTurn == NOT_STARTED) {
			throw new IllegalStateException("Battle hasn't started");
		}
		turns.add(pTurnData);
		currentTurn++;
		currentTurnInstance = pTurnData;
		return currentTurnInstance;
	}

	public synchronized void finish() {
		currentTurn = FINISHED;
		currentTurnInstance = new Turn(FINISHED, null);
	}

	public synchronized List<Turn> allTurns() {
		return List.copyOf(turns);
	}
}
