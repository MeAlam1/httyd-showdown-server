package com.mealam.showdown.battle.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mealam.showdown.data.BaseId;
import com.mealam.showdown.utils.IdGenerator;

public class BattleId extends BaseId {

	@JsonCreator
	public BattleId(String pValue) {
		super(pValue);
	}

	public static BattleId generate() {
		return new BattleId(IdGenerator.generateId());
	}

	public static BattleId parse(String pRaw) {
		return new BattleId(pRaw);
	}
}