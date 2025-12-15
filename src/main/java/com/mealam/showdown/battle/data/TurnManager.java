package com.mealam.showdown.battle.data;

import com.mealam.showdown.battle.context.TurnContext;
import com.mealam.showdown.user.data.UserId;

public class TurnManager {

	public static final int NOT_STARTED = 0;
	public static final int FINISHED = -1;

	private volatile int currentTurn;
	private volatile TurnContext currentTurnContextInstance;

	public synchronized TurnContext createBattle() {
		currentTurn = NOT_STARTED;
		currentTurnContextInstance = new TurnContext(NOT_STARTED, null, null);
		return currentTurnContextInstance;
	}

	public synchronized TurnContext startBattle(UserId pStartingPlayerId) {
		if (currentTurn != NOT_STARTED) {
			throw new IllegalStateException("Battle already started or finished");
		}
		currentTurn = 1;
		currentTurnContextInstance = new TurnContext(currentTurn, null, pStartingPlayerId);
		return currentTurnContextInstance;
	}

	public synchronized TurnContext advance(UserId pNextActivePlayerId) {
		if (currentTurn == FINISHED) {
			throw new IllegalStateException("Battle is over");
		}
		if (currentTurn == NOT_STARTED) {
			if (currentTurnContextInstance == null || currentTurnContextInstance.activePlayerId() == null) {
				throw new IllegalStateException("Battle hasn't started");
			}
			currentTurn = 1;
		} else {
			currentTurn++;
		}
		currentTurnContextInstance = new TurnContext(currentTurn, null, pNextActivePlayerId);
		return currentTurnContextInstance;
	}

	public synchronized void finish() {
		currentTurn = FINISHED;
		currentTurnContextInstance = new TurnContext(FINISHED, null, null);
	}
}