package com.mealam.showdown.battle.http;

import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.context.TurnContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.request.TurnBattleRequest;
import com.mealam.showdown.battle.dto.response.CreateBattleResponse;
import com.mealam.showdown.battle.infra.ws.BattleWebSocket;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

import java.util.Map;
import java.util.UUID;

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
			if (BattleWebSocket.get() != null) BattleWebSocket.get().publish(battle);
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
			if (BattleWebSocket.get() != null) BattleWebSocket.get().publish(battle);
		} catch (IllegalStateException pIllegalStateException) {
			ResponseUtils.badRequest(pContext, pIllegalStateException.getMessage(), "battle_start_invalid_state");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error starting battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "battle_start_error");
		}
	}

	public void advanceTurn(Context pContext) {
		final String traceId = UUID.randomUUID().toString();
		final String rawBattleId = pContext.pathParam("id");
		final String authHeader = pContext.header("Authorization");

		BaseLogger.log(BaseLogLevel.INFO,
				"[traceId=" + traceId + "] turn.advance -> request battleId=" + rawBattleId
						+ " method=" + pContext.method()
						+ " path=" + pContext.path()
						+ " hasAuth=" + (authHeader != null));

		try {
			var id = BattleId.parse(rawBattleId);

			var existing = service.getBattle(id);
			if (existing == null) {
				BaseLogger.log(BaseLogLevel.WARNING,
						"[traceId=" + traceId + "] turn.advance -> battle not found battleId=" + id);
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}

			TurnContext ctx = existing.turnContext();
			BaseLogger.log(BaseLogLevel.INFO,
					"[traceId=" + traceId + "] turn.advance -> loaded battle"
							+ " battleId=" + id
							+ " turnStatus=" + (ctx == null ? "null" : ctx.status())
							+ " turnNumber=" + (ctx == null ? "null" : ctx.turnNumber())
							+ " activeTeamId=" + (ctx == null ? "null" : ctx.activeTeamId())
							+ " teams=" + (existing.teams() == null ? "null" : existing.teams().size())
							+ " spectators=" + (existing.spectatorIds() == null ? "null" : existing.spectatorIds().size()));

			if (ctx == null || ctx.status() == TurnContext.TurnStatus.NOT_STARTED) {
				BaseLogger.log(BaseLogLevel.WARNING,
						"[traceId=" + traceId + "] turn.advance -> rejected: battle not started battleId=" + id);
				ResponseUtils.badRequest(pContext, "Battle not started", "battle_not_started");
				return;
			}
			if (ctx.status() == TurnContext.TurnStatus.FINISHED) {
				BaseLogger.log(BaseLogLevel.WARNING,
						"[traceId=" + traceId + "] turn.advance -> rejected: battle finished battleId=" + id);
				ResponseUtils.badRequest(pContext, "Battle already finished", "battle_already_finished");
				return;
			}

			UserId actingUserId = null;
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String tokenUser = authHeader.substring("Bearer ".length()).trim();
				if (!tokenUser.isBlank()) {
					try {
						actingUserId = UserId.parse(tokenUser);
					} catch (Exception ex) {
						BaseLogger.log(BaseLogLevel.WARNING,
								"[traceId=" + traceId + "] turn.advance -> invalid bearer token value=" + tokenUser, ex);
						ResponseUtils.badRequest(pContext, "Invalid Authorization bearer token", "invalid_authorization");
						return;
					}
				}
			}
			if (actingUserId == null) {
				BaseLogger.log(BaseLogLevel.WARNING,
						"[traceId=" + traceId + "] turn.advance -> rejected: missing acting user");
				ResponseUtils.badRequest(pContext, "Acting user is required", "acting_user_required");
				return;
			}

			TurnBattleRequest turnReq;
			try {
				turnReq = pContext.bodyAsClass(TurnBattleRequest.class);
			} catch (Exception pException) {
				BaseLogger.log(BaseLogLevel.WARNING,
						"[traceId=" + traceId + "] turn.advance -> invalid JSON payload for actingUserId=" + actingUserId
								+ " battleId=" + id, pException);
				ResponseUtils.badRequest(pContext, "Invalid turn payload", "turn_payload_invalid");
				return;
			}

			BaseLogger.log(BaseLogLevel.INFO,
					"[traceId=" + traceId + "] turn.advance -> calling service"
							+ " battleId=" + id
							+ " actingUserId=" + actingUserId
							+ " action=" + (turnReq == null ? "null" : turnReq.action()));

			var battle = service.advanceTurn(id, actingUserId, turnReq);
			if (battle == null) {
				BaseLogger.log(BaseLogLevel.WARNING,
						"[traceId=" + traceId + "] turn.advance -> service returned null battleId=" + id);
				ResponseUtils.notFound(pContext, "Battle not found", "battle_not_found");
				return;
			}

			var newCtx = battle.turnContext();
			BaseLogger.log(BaseLogLevel.INFO,
					"[traceId=" + traceId + "] turn.advance -> success"
							+ " battleId=" + id
							+ " newTurnNumber=" + (newCtx == null ? "null" : newCtx.turnNumber())
							+ " newActiveTeamId=" + (newCtx == null ? "null" : newCtx.activeTeamId())
							+ " submissions=" + (newCtx == null || newCtx.submissions() == null ? "null" : newCtx.submissions().size())
							+ " turnHistory=" + (battle.turnHistory() == null ? "null" : battle.turnHistory().size()));

			// \*\*Pinpoint whether JSON serialization is failing\*\*
			try {
				var response = new CreateBattleResponse(battle);

				Object submissions = (newCtx == null ? null : newCtx.submissions());
				String submissionsKeyType = null;
				if (submissions instanceof Map<?, ?> m && !m.isEmpty()) {
					Object k = m.keySet().iterator().next();
					submissionsKeyType = (k == null ? "null" : k.getClass().getName());
				}

				Object firstEvent = null;
				if (battle.turnHistory() != null && !battle.turnHistory().isEmpty()) {
					var last = battle.turnHistory().getLast();
					if (last.events() != null && !last.events().isEmpty()) {
						firstEvent = last.events().getFirst();
					}
				}

				BaseLogger.log(BaseLogLevel.INFO,
						"[traceId=" + traceId + "] turn.advance -> responding"
								+ " battleId=" + id
								+ " submissionsKeyType=" + submissionsKeyType
								+ " firstEventType=" + (firstEvent == null ? "null" : firstEvent.getClass().getName()));

				ResponseUtils.ok(pContext, response);
				BaseLogger.log(BaseLogLevel.INFO,
						"[traceId=" + traceId + "] turn.advance -> response written battleId=" + id);
			} catch (Exception ex) {
				BaseLogger.log(BaseLogLevel.ERROR,
						"[traceId=" + traceId + "] turn.advance -> response serialization/write failed battleId=" + id
								+ " exType=" + ex.getClass().getName()
								+ " msg=" + ex.getMessage(), ex);
				ResponseUtils.serverError(pContext, "Internal server error", "turn_advance_response_error");
				return;
			}

			try {
				if (BattleWebSocket.get() != null) {
					BattleWebSocket.get().publish(battle);
					BaseLogger.log(BaseLogLevel.INFO,
							"[traceId=" + traceId + "] turn.advance -> websocket published battleId=" + id);
				}
			} catch (Exception ex) {
				BaseLogger.log(BaseLogLevel.ERROR,
						"[traceId=" + traceId + "] turn.advance -> websocket publish failed battleId=" + id
								+ " exType=" + ex.getClass().getName()
								+ " msg=" + ex.getMessage(), ex);
			}

		} catch (IllegalStateException | IllegalArgumentException pIllegalException) {
			BaseLogger.log(BaseLogLevel.WARNING,
					"[traceId=" + traceId + "] turn.advance -> rejected: " + pIllegalException.getMessage(), pIllegalException);
			ResponseUtils.badRequest(pContext, pIllegalException.getMessage(), "turn_advance_invalid");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR,
					"[traceId=" + traceId + "] turn.advance -> server error battleId=" + rawBattleId
							+ " exType=" + pException.getClass().getName()
							+ " msg=" + pException.getMessage(), pException);
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
			if (BattleWebSocket.get() != null) BattleWebSocket.get().publish(battle);
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
			var battle = service.getBattle(id);
			if (battle != null && BattleWebSocket.get() != null) BattleWebSocket.get().publish(battle);
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
			if (BattleWebSocket.get() != null) BattleWebSocket.get().publish(battle);
		} catch (IllegalArgumentException pIllegalArgumentException) {
			ResponseUtils.badRequest(pContext, "Invalid request", "leave_invalid_request");
		} catch (Exception pException) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error leaving battle", pException);
			ResponseUtils.serverError(pContext, "Internal server error", "leave_error");
		}
	}
}