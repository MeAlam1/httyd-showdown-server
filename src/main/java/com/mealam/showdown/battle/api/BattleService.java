package com.mealam.showdown.battle.api;

import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.request.TurnBattleRequest;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import com.mealam.showdown.user.data.UserId;

public interface BattleService {
	BattleContext createBattle(CreateBattleRequest pRequest);

	BattleContext startBattle(BattleId pBattleId);

	BattleContext advanceTurn(BattleId pBattleId, UserId pActingUserId, TurnBattleRequest pTurnData);

	BattleContext finishBattle(BattleId pBattleId, UserId pWinnerId);

	BattleContext getBattle(BattleId pBattleId);

	JoinBattleResponse joinBattle(BattleId pBattleId, JoinBattleRequest pRequest);

	BattleContext leaveBattle(BattleId pBattleId, LeaveBattleRequest pRequest);
}