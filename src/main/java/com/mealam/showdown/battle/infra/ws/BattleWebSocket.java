package com.mealam.showdown.battle.infra.ws;

import com.mealam.showdown.battle.api.BattleService;
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

	private final Map<BattleId, Set<WsContext>> sessions = new ConcurrentHashMap<>();
	private final BattleService service;

	public BattleWebSocket(BattleService pService) {
		this.service = pService;
	}

	public void configure(WsConfig pConfig) {
		pConfig.onConnect(pContext -> {
			try {
				BattleId battleId = extractBattleId(pContext);
				sessions.computeIfAbsent(battleId, k -> ConcurrentHashMap.newKeySet()).add(pContext);
				WebSocketUtils.send(pContext, WebSocketMessage.of("connected", null));
				BaseLogger.log(BaseLogLevel.INFO, "Client connected to battle: " + battleId);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.ERROR, "Error during connection", e);
				safeError(pContext, "connection_error");
			}
		});

		pConfig.onMessage(pContext -> {
			try {
				BattleId battleId = extractBattleId(pContext);
				String message = pContext.message();

				BattleContext battle = service.getBattle(battleId);
				if (battle == null) {
					safeError(pContext, "battle_not_found");
					return;
				}

				WebSocketMessage msg = WebSocketMessage.of("update", message);
				broadcast(battleId, msg);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.ERROR, "Error processing message", e);
				safeError(pContext, "message_processing_error");
			}
		});

		pConfig.onClose(pContext -> {
			try {
				BattleId battleId = extractBattleId(pContext);
				removeSession(battleId, pContext);
				BaseLogger.log(BaseLogLevel.INFO, "Client disconnected from battle: " + battleId);
			} catch (Exception e) {
				BaseLogger.log(BaseLogLevel.ERROR, "Error during disconnection", e);
			}
		});
	}

	private BattleId extractBattleId(WsContext pContext) {
		return BattleId.parse(pContext.pathParam("id"));
	}

	private void broadcast(BattleId pBattleId, WebSocketMessage pMessage) {
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

	private void removeSession(BattleId pBattleId, WsContext pContext) {
		Set<WsContext> sessionSet = sessions.get(pBattleId);
		if (sessionSet != null) {
			sessionSet.remove(pContext);
			if (sessionSet.isEmpty()) {
				sessions.remove(pBattleId);
			}
		}
	}

	private void safeError(WsContext pContext, String pCode) {
		try {
			WebSocketUtils.send(pContext, WebSocketMessage.of("error", pCode));
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.ERROR, "Failed to send error message", e);
		}
	}
}