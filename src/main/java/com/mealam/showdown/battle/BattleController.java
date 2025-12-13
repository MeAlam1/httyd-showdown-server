package com.mealam.showdown.battle;

import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import io.javalin.http.Context;

import java.util.Map;

public class BattleController {

	private final BattleService service = new BattleService();

	public void createBattle(Context pContext) {
		var request = pContext.bodyAsClass(CreateBattleRequest.class);
		var battle = service.createBattle(request);
		pContext.json(Map.of("id", battle.battleId().toString()));
	}

	public void getBattle(Context pContext) {
		var id = BattleId.parse(pContext.pathParam("id"));
		var battle = service.getBattle(id);

		if (battle == null) {
			pContext.status(404).result("Battle not found");
			return;
		}

		pContext.json(battle);
	}

	public void joinBattle(Context pContext) {
		var id = BattleId.parse(pContext.pathParam("id"));
		var req = pContext.bodyAsClass(JoinBattleRequest.class);

		var battle = service.joinBattle(id, req);
		if (battle == null) {
			pContext.status(404).result("Battle not found");
			return;
		}

		pContext.json(Map.of("id", battle.battleId().toString()));
	}

	public void leaveBattle(Context pContext) {
		var id = BattleId.parse(pContext.pathParam("id"));
		var req = pContext.bodyAsClass(LeaveBattleRequest.class);

		var battle = service.leaveBattle(id, req);
		if (battle == null) {
			pContext.status(404).result("Battle not found");
			return;
		}

		pContext.json(Map.of("id", battle.battleId().toString()));
	}
}