package com.mealam.showdown.move.data;

import com.mealam.showdown.api.utils.IdGenerator;
import com.mealam.showdown.data.BaseId;

public class MoveId extends BaseId {

	private MoveId(String pValue) {
		super(pValue);
	}

	public static MoveId generate() {
		return new MoveId(IdGenerator.generateId());
	}

	public static MoveId parse(String pRaw) {
		return new MoveId(pRaw);
	}
}
