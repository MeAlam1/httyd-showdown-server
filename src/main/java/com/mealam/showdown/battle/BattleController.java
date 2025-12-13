package com.mealam.showdown.battle;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BattleController {

	private static final Logger logger = LoggerFactory.getLogger(BattleController.class);
	private final BattleService service = new BattleService();

	public void createBattle(Context pContext) {
		try {
			var request = pContext.bodyAsClass(CreateBattleRequest.class);
			var battle = service.createBattle(request);
			pContext.status(201).json(Map.of("id", battle.battleId().toString()));
		} catch (Exception e) {
			logger.error("Error creating battle", e);
			pContext.status(500).json(Map.of("error", "Failed to create battle"));
		}
	}

	public void getBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var battle = service.getBattle(id);

			if (battle == null) {
				pContext.status(404).json(Map.of("error", "Battle not found"));
				return;
			}

			pContext.json(battle);
		} catch (IllegalArgumentException e) {
			pContext.status(400).json(Map.of("error", "Invalid battle ID"));
		} catch (Exception e) {
			logger.error("Error retrieving battle", e);
			pContext.status(500).json(Map.of("error", "Internal server error"));
		}
	}

	public void joinBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(JoinBattleRequest.class);

			JoinBattleResponse resp = service.joinBattle(id, req);
			if (resp == null) {
				pContext.status(404).json(Map.of("error", "Battle not found"));
				return;
			}

			pContext.json(resp);
		} catch (IllegalArgumentException e) {
			pContext.status(400).json(Map.of("error", "Invalid request"));
		} catch (Exception e) {
			logger.error("Error joining battle", e);
			pContext.status(500).json(Map.of("error", "Internal server error"));
		}
	}

	public void leaveBattle(Context pContext) {
		try {
			var id = BattleId.parse(pContext.pathParam("id"));
			var req = pContext.bodyAsClass(LeaveBattleRequest.class);

			var battle = service.leaveBattle(id, req);
			if (battle == null) {
				pContext.status(404).json(Map.of("error", "Battle not found"));
				return;
			}

			pContext.json(Map.of("id", battle.battleId().toString()));
		} catch (IllegalArgumentException e) {
			pContext.status(400).json(Map.of("error", "Invalid request"));
		} catch (Exception e) {
			logger.error("Error leaving battle", e);
			pContext.status(500).json(Map.of("error", "Internal server error"));
		}
	}
}