package com.mealam.showdown.battle;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.turns.TurnContext;
import com.mealam.showdown.battle.data.turns.TurnManager;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.response.*;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

public class BattleController {

	private final BattleService service = new BattleService();

	public void createBattle(Context ctx) {
		try {
			var req = ctx.bodyAsClass(CreateBattleRequest.class);
			var battle = service.createBattle(req);
			ResponseUtils.created(ctx, BattleSummaryResponse.from(battle));
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error creating battle", e);
			ResponseUtils.serverError(ctx, "Failed to create battle", "battle_create_failed");
		}
	}

	public void getBattle(Context ctx) {
		try {
			var id = BattleId.parse(ctx.pathParam("id"));
			var battle = service.getBattle(id);
			if (battle == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(ctx, BattleSummaryResponse.from(battle));
		} catch (IllegalArgumentException e) {
			ResponseUtils.badRequest(ctx, "Invalid battle ID", "invalid_battle_id");
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving battle", e);
			ResponseUtils.serverError(ctx, "Internal server error", "battle_get_error");
		}
	}

	public void startBattle(Context ctx) {
		try {
			var id = BattleId.parse(ctx.pathParam("id"));
			var battle = service.startBattle(id);
			if (battle == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(ctx, BattleSummaryResponse.from(battle));
		} catch (IllegalStateException e) {
			ResponseUtils.badRequest(ctx, e.getMessage(), "battle_start_invalid_state");
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error starting battle", e);
			ResponseUtils.serverError(ctx, "Internal server error", "battle_start_error");
		}
	}

	public void advanceTurn(Context ctx) {
		try {
			var id = BattleId.parse(ctx.pathParam("id"));
			var existing = service.getBattle(id);
			if (existing == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}
			if (existing.turnContext() == null || existing.turnContext().turnNumber() == TurnManager.NOT_STARTED) {
				ResponseUtils.badRequest(ctx, "Battle not started", "battle_not_started");
				return;
			}
			if (existing.turnContext().turnNumber() == TurnManager.FINISHED) {
				ResponseUtils.badRequest(ctx, "Battle already finished", "battle_already_finished");
				return;
			}

			TurnContext turnData = null;
			try {
				turnData = ctx.bodyAsClass(TurnContext.class);
			} catch (Exception ignored) {
				// optional body
			}

			var battle = service.advanceTurn(id, turnData);
			if (battle == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(ctx, TurnAdvanceResponse.from(battle));
		} catch (IllegalStateException | IllegalArgumentException e) {
			BaseLogger.log(BaseLogLevel.WARNING, "Invalid state: " + e.getMessage());
			ResponseUtils.badRequest(ctx, e.getMessage(), "turn_advance_invalid");
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error advancing turn", e);
			ResponseUtils.serverError(ctx, "Internal server error", "turn_advance_error");
		}
	}

	public void finishBattle(Context ctx) {
		try {
			var id = BattleId.parse(ctx.pathParam("id"));
			String winnerIdStr = ctx.queryParam("winnerId");
			UserId winnerId = winnerIdStr != null ? UserId.parse(winnerIdStr) : null;

			var battle = service.finishBattle(id, winnerId);
			if (battle == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(ctx, BattleSummaryResponse.from(battle));
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error finishing battle", e);
			ResponseUtils.serverError(ctx, "Internal server error", "battle_finish_error");
		}
	}

	public void joinBattle(Context ctx) {
		try {
			var id = BattleId.parse(ctx.pathParam("id"));
			var req = ctx.bodyAsClass(JoinBattleRequest.class);

			var resp = service.joinBattle(id, req);
			if (resp == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}

			ResponseUtils.ok(ctx, resp);
		} catch (IllegalArgumentException e) {
			ResponseUtils.badRequest(ctx, "Invalid request", "join_invalid_request");
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error joining battle", e);
			ResponseUtils.serverError(ctx, "Internal server error", "join_error");
		}
	}

	public void leaveBattle(Context ctx) {
		try {
			var id = BattleId.parse(ctx.pathParam("id"));
			var req = ctx.bodyAsClass(LeaveBattleRequest.class);

			var battle = service.leaveBattle(id, req);
			if (battle == null) {
				ResponseUtils.notFound(ctx, "Battle not found", "battle_not_found");
				return;
			}
			ResponseUtils.ok(ctx, BattleSummaryResponse.from(battle));
		} catch (IllegalArgumentException e) {
			ResponseUtils.badRequest(ctx, "Invalid request", "leave_invalid_request");
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error leaving battle", e);
			ResponseUtils.serverError(ctx, "Internal server error", "leave_error");
		}
	}
}