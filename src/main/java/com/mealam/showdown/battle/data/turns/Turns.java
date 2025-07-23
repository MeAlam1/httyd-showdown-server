/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.data.turns;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Turns {

	public static final int NOT_STARTED = 0;
	public static final int FINISHED = -1;

	private final List<Turn> turns = new ArrayList<>();
	private int currentTurn;
	private Turn currentTurnInstance;

	public Turn createBattle() {
		currentTurn = NOT_STARTED;
		turns.clear();
		return new Turn(NOT_STARTED, null);
	}

	public Turn startBattle() {
		if (currentTurn != NOT_STARTED) {
			throw new IllegalStateException("Battle already started or finished");
		}
		currentTurn = 1;
		return new Turn(currentTurn, null);
	}

	public Turn advance(Turn pTurnData) {
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

	public void finish() {
		currentTurn = FINISHED;
		currentTurnInstance = new Turn(FINISHED, null);
	}

	public int getCurrentTurnNumber() {
		return currentTurn;
	}

	public boolean isOngoing() {
		return currentTurn > 0;
	}

	public boolean isFinished() {
		return currentTurn == FINISHED;
	}

	public List<Turn> allTurns() {
		return List.copyOf(turns);
	}

	public Optional<Turn> getTurn(int pNumber) {
		return turns.stream().filter(t -> t.number() == pNumber).findFirst();
	}

	public Optional<Turn> getCurrentTurn() {
		return Optional.ofNullable(currentTurnInstance);
	}
}
