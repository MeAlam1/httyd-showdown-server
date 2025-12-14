package com.mealam.showdown.utils.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.WsContext;

public final class WebSocketUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

	private WebSocketUtils() {
	}

	public static void send(WsContext pWsContext, WebSocketMessage pWebSocketMessage) {
		try {
			pWsContext.send(mapper.writeValueAsString(pWebSocketMessage));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}