/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.WsContext;

public final class WebSocketUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

	private WebSocketUtils() {}

	public static void send(WsContext pWsContext, WebSocketMessage pWebSocketMessage) {
		try {
			pWsContext.send(mapper.writeValueAsString(pWebSocketMessage));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
