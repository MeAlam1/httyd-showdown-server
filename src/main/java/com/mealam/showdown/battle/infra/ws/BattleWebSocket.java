/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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

	private static volatile BattleWebSocket INSTANCE;

	private final Map<BattleId, Set<WsContext>> sessions = new ConcurrentHashMap<>();
	private final BattleService service;

	public BattleWebSocket(BattleService pService) {
		this.service = pService;
		INSTANCE = this;
	}

	public static BattleWebSocket get() {
		return INSTANCE;
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

				WebSocketMessage echo = WebSocketMessage.of("update", message);
				broadcast(battleId, echo);

				WebSocketMessage state = WebSocketMessage.of("state", battle);
				broadcast(battleId, state);
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

	public void publish(BattleContext pBattle) {
		if (pBattle == null) return;
		try {
			WebSocketMessage state = WebSocketMessage.of("state", pBattle);
			broadcast(pBattle.battleId(), state);
		} catch (Exception e) {
			BaseLogger.log(BaseLogLevel.WARNING, "Failed to publish battle state", e);
		}
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
