package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import com.mealam.showdown.utils.ws.WebSocketMessage;
import com.mealam.showdown.utils.ws.WebSocketUtils;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BattleWebSocket {

	private static final Map<BattleId, Set<WsContext>> sessions = new ConcurrentHashMap<>();
	private static final BattleService service = new BattleService();

	public static void configure(WsConfig pConfig) {
		pConfig.onConnect(ctx -> {
			try {
				BattleId battleId = extractBattleId(ctx);
				sessions.computeIfAbsent(battleId, k -> ConcurrentHashMap.newKeySet()).add(ctx);
				WebSocketUtils.send(ctx, WebSocketMessage.of("connected", null));
				BaseLogger.log(BaseLogLevel.INFO, "Client connected to battle: " + battleId);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.ERROR, "Error during connection", e);
				safeError(ctx, "connection_error");
			}
		});

		pConfig.onMessage(ctx -> {
			try {
				BattleId battleId = extractBattleId(ctx);
				String message = ctx.message();

				BattleContext battle = service.getBattle(battleId);
				if (battle == null) {
					safeError(ctx, "battle_not_found");
					return;
				}

				WebSocketMessage msg = WebSocketMessage.of("update", message);
				broadcast(battleId, msg);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.ERROR, "Error processing message", e);
				safeError(ctx, "message_processing_error");
			}
		});

		pConfig.onClose(ctx -> {
			try {
				BattleId battleId = extractBattleId(ctx);
				removeSession(battleId, ctx);
				BaseLogger.log(BaseLogLevel.INFO, "Client disconnected from battle: " + battleId);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.ERROR, "Error during disconnection", e);
			}
		});
	}

	private static BattleId extractBattleId(WsContext pContext) {
		return BattleId.parse(pContext.pathParam("id"));
	}

	private static void broadcast(BattleId pBattleId, WebSocketMessage pMessage) {
		Set<WsContext> sessionSet = sessions.get(pBattleId);
		if (sessionSet == null || sessionSet.isEmpty()) return;

		for (WsContext ctx : Set.copyOf(sessionSet)) {
			try {
				WebSocketUtils.send(ctx, pMessage);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.WARNING, "Failed to send message to client, removing session", e);
				removeSession(pBattleId, ctx);
			}
		}
	}

	private static void removeSession(BattleId pBattleId, WsContext pContext) {
		Set<WsContext> sessionSet = sessions.get(pBattleId);
		if (sessionSet != null) {
			sessionSet.remove(pContext);
			if (sessionSet.isEmpty()) {
				sessions.remove(pBattleId);
			}
		}
	}

	private static void safeError(WsContext ctx, String code) {
		try {
			WebSocketUtils.send(ctx, WebSocketMessage.of("error", code));
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to send error message", e);
		}
	}
}