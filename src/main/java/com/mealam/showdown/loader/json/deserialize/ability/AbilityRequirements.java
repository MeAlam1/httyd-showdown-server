/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.loader.json.deserialize.ability;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.mealam.showdown.utils.json.deserialize.RecordJsonDeserializer;

/**
 * Examples:
 * <code>
 * "requirements": {
 * "preBattle": {
 * "when": "hasItem(self, 'toxin_canister')",
 * "onFail": "suppressAbility"
 * },
 * "onEnter": {
 * "when": "self.healthPct > 0.3",
 * "onFail": "delay"
 * },
 * "perTarget": {
 * "when": "!hasTag(target, 'construct') && !hasTag(target, 'ethereal')",
 * "onFail": "skipTarget"
 * },
 * "battleRules": {
 * "when": "battle.mode != 'safe_zone' && !battle.flags.noStatusEffects",
 * "onFail": "abort"
 * },
 * "consumption": {
 * "when": "true",
 * "consume": {
 * "item": "toxin_canister",
 * "amount": 1
 * }
 * }
 * }
 * </code>
 * or
 * <code>
 * "requirements": {
 * "when": "hasAnyTag(self, ['toxic','venomous'])",
 * "battle": "battle.mode != 'tutorial'",
 * "environment": "!contains(battle.weather, 'wind')"
 * }
 * </code>
 * <p>
 * TODO: Create an external system that holds all possible requirement and conditions, kinda like an ExtendableEnum.
 */
public record AbilityRequirements() {

	public static JsonDeserializer<AbilityRequirements> deserializer() {
		return new Deserializer();
	}

	private static final class Deserializer extends RecordJsonDeserializer<AbilityRequirements> {

		@Override
		protected AbilityRequirements deserializeObject(JsonObject pObj, JsonDeserializationContext pContext) {
			return new AbilityRequirements();
		}

		@Override
		protected String targetTypeName() {
			return AbilityRequirements.class.getSimpleName();
		}
	}
}
