package com.mealam.showdown.battle;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.turns.TurnContext;
import com.mealam.showdown.battle.data.turns.TurnManager;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.json.JSONFormatUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

public class BattleController {

	private final BattleService service = new BattleService();

	public void createBattle(Context pContext) {
		try {
			var request = pContext.bodyAsClass(CreateBattleRequest.class);
			var battle = service.createBattle(request);
			pContext.status(201).result(JSONFormatUtils.createJsonMessage("id", battle.battleId().toString()));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error creating battle", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Failed to create battle"));
		}
	}

	public void getBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var battle = service.getBattle(id);

			if (battle == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}

			pContext.json(battle);
		} catch (IllegalArgumentException pIllegalArgumentException) {
			pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", "Invalid battle ID"));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving battle", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Internal server error"));
		}
	}

	public void startBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var battle = service.startBattle(id);

			if (battle == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}

			pContext.json(battle);
		} catch (IllegalStateException e) {
			pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", e.getMessage()));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error starting battle", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Internal server error"));
		}
	}

	// java
	public void advanceTurn(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));

			var existing = service.getBattle(id);
			if (existing == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}
			if (existing.turnContext() == null || existing.turnContext().turnNumber() == TurnManager.NOT_STARTED) {
				pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", "Battle not started"));
				return;
			}
			if (existing.turnContext().turnNumber() == TurnManager.FINISHED) {
				pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", "Battle already finished"));
				return;
			}

			TurnContext turnData = null;
			try {
				turnData = pContext.bodyAsClass(TurnContext.class);
			} catch (Exception ignored) {
			}

			var battle = service.advanceTurn(id, turnData);

			if (battle == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}

			pContext.json(battle);
		} catch (IllegalStateException | IllegalArgumentException e) {
			BaseLogger.log(BaseLogLevel.WARNING, "Invalid state: " + e.getMessage());
			pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", e.getMessage()));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error advancing turn", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Internal server error"));
		}
	}

	public void finishBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			String winnerIdStr = pContext.queryParam("winnerId");
			UserId winnerId = winnerIdStr != null ? UserId.parse(winnerIdStr) : null;

			var battle = service.finishBattle(id, winnerId);

			if (battle == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}

			pContext.json(battle);
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error finishing battle", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Internal server error"));
		}
	}

	public void joinBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(JoinBattleRequest.class);

			JoinBattleResponse resp = service.joinBattle(id, req);
			if (resp == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}

			pContext.status(200).result(JSONFormatUtils.createJsonMessage("id", id.toString()));
		} catch (IllegalArgumentException e) {
			pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", "Invalid request"));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error joining battle", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Internal server error"));
		}
	}

	public void leaveBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(LeaveBattleRequest.class);

			var battle = service.leaveBattle(id, req);
			if (battle == null) {
				pContext.status(404).result(JSONFormatUtils.createJsonMessage("error", "Battle not found"));
				return;
			}

			pContext.result(JSONFormatUtils.createJsonMessage("id", battle.battleId().toString()));
		} catch (IllegalArgumentException pIllegalArgumentException) {
			pContext.status(400).result(JSONFormatUtils.createJsonMessage("error", "Invalid request"));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error leaving battle", pException);
			pContext.status(500).result(JSONFormatUtils.createJsonMessage("error", "Internal server error"));
		}
	}
}