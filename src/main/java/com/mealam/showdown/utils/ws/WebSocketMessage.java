package com.mealam.showdown.utils.ws;

public record WebSocketMessage(
		String type,
		Object payload
) {
	public static WebSocketMessage of(String pType, Object pPayload) {
		return new WebSocketMessage(pType, pPayload);
	}
}