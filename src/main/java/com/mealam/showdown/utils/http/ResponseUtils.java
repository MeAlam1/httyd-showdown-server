package com.mealam.showdown.utils.http;

import com.mealam.showdown.utils.http.dto.ApiResponse;
import com.mealam.showdown.utils.http.dto.ErrorResponse;
import io.javalin.http.Context;

public final class ResponseUtils {

	private ResponseUtils() {
	}

	public static <T> void ok(Context pContext, T pData) {
		pContext.status(200).json(ApiResponse.success(pData));
	}

	public static <T> void created(Context pContext, T pData) {
		pContext.status(201).json(ApiResponse.success(pData));
	}

	public static void notFound(Context pContext, String pMessage, String pCode) {
		pContext.status(404).json(ErrorResponse.of(pMessage, pCode));
	}

	public static void badRequest(Context pContext, String pMessage, String pCode) {
		pContext.status(400).json(ErrorResponse.of(pMessage, pCode));
	}

	public static void serverError(Context pContext, String pMessage, String pCode) {
		pContext.status(500).json(ErrorResponse.of(pMessage, pCode));
	}
}