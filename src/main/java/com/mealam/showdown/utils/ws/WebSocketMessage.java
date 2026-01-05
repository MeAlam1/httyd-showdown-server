/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.ws;

public record WebSocketMessage(
		String type,
		Object payload) {

	public static WebSocketMessage of(String pType, Object pPayload) {
		return new WebSocketMessage(pType, pPayload);
	}
}
