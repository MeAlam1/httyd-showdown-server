package com.mealam.showdown.battle.http;

import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.turns.TurnManager;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.request.TurnBattleRequest;
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

public class BattleController {

	private final BattleService service;

	public BattleController(BattleService pService) {
		this.service = pService;
	}

	public void createBattle(Context pContext) {
		try {
			var req = pContext.bodyAsClass(CreateBattleRequest.class);
			var battle = service.createBattle(req);
			ResponseUtils.created(pContext, new CreateBattleResponse(battle));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error creating battle", pException);
			ResponseUtils.serverError(pContext, "Failed to create battle", "battle_create_failed");
		}
	}

	public void getBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var battle = service.getBattle(id);
			if (battle == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new CreateBattleResponse(battle));
		} catch (IllegalArgumentException pIllegalArgumentException) {
			ResponseUtils.badRequest(pContext, "Invalid battle ID", "invalid_battle_id");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "battle_get_error");
		}
	}

	public void startBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var battle = service.startBattle(id);
			if (battle == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new CreateBattleResponse(battle));
		} catch (IllegalStateException pIllegalStateException) {
			ResponseUtils.badRequest(pContext, pIllegalStateException.getMessage(), "battle_start_invalid_state");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error starting battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "battle_start_error");
		}
	}

	public void advanceTurn(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var existing = service.getBattle(id);
			if (existing == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			if (existing.turnContext() == null || existing.turnContext().turnNumber() == TurnManager.NOT_STARTED) {
				ResponseUtils.badRequest(pContext, "Battle not started", "battle_not_started");
				return;
			}
			if (existing.turnContext().turnNumber() == TurnManager.FINISHED) {
				ResponseUtils.badRequest(pContext, "Battle already finished", "battle_already_finished");
				return;
			}

			TurnBattleRequest turnReq = null;
			try {
				turnReq = pContext.bodyAsClass(TurnBattleRequest.class);
			} catch (Exception pException) {
				ResponseUtils.badRequest(pContext, "Invalid turn payload", "turn_payload_invalid");
				return;
			}

			var battle = service.advanceTurn(id, turnReq);
			if (battle == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new CreateBattleResponse(battle));
		} catch (IllegalStateException | IllegalArgumentException pIllegalException) {
			BaseLogger.log(BaseLogLevel.WARNING, "Invalid state: " + pIllegalException.getMessage());
			ResponseUtils.badRequest(pContext, pIllegalException.getMessage(), "turn_advance_invalid");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error advancing turn", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "turn_advance_error");
		}
	}

	public void finishBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			String winnerIdStr = pContext.queryParam("winnerId");
			var winnerId = winnerIdStr != null ? UserId.parse(winnerIdStr) : null;

			var battle = service.finishBattle(id, winnerId);
			if (battle == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new CreateBattleResponse(battle));
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error finishing battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "battle_finish_error");
		}
	}

	public void joinBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(JoinBattleRequest.class);

			var resp = service.joinBattle(id, req);
			if (resp == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(pContext, resp);
		} catch (IllegalArgumentException pIllegalArgumentException) {
			ResponseUtils.badRequest(pContext, "Invalid request", "join_invalid_request");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error joining battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "join_error");
		}
	}

	public void leaveBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(LeaveBattleRequest.class);

			var battle = service.leaveBattle(id, req);
			if (battle == null) {
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(pContext, new CreateBattleResponse(battle));
		} catch (IllegalArgumentException pIllegalArgumentException) {
			ResponseUtils.badRequest(pContext, "Invalid request", "leave_invalid_request");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error leaving battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "leave_error");
		}
	}
}