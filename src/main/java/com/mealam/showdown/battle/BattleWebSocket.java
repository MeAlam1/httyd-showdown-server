package com.mealam.showdown.battle;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
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
			BattleId battleId = extractBattleId(ctx);
			sessions.computeIfAbsent(battleId, k -> ConcurrentHashMap.newKeySet()).add(ctx);

			ctx.send("{\"type\":\"connected\"}");
		});

		pConfig.onMessage(ctx -> {
			BattleId battleId = extractBattleId(ctx);
			var msg = ctx.message();

			BattleContext battle = service.getBattle(battleId);
			if (battle == null) {
				ctx.send("{\"error\":\"battle_not_found\"}");
				return;
			}

			broadcast(battleId, "{\"type\":\"update\",\"msg\":\"" + msg + "\"}");
		});

		pConfig.onClose(ctx -> {
			BattleId battleId = extractBattleId(ctx);
			var set = sessions.get(battleId);
			if (set != null) set.remove(ctx);
		});
	}

	private static BattleId extractBattleId(WsContext pContext) {
		return BattleId.parse(pContext.pathParam("id"));
	}

	private static void broadcast(BattleId pBattleId, String pMessage) {
		var set = sessions.get(pBattleId);
		if (set == null) return;

		for (WsContext c : set) {
			c.send(pMessage);
		}
	}
}
