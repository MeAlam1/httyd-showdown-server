package com.mealam.showdown.utils.http.dto;

public sealed interface ApiResponse<T> permits SuccessResponse, ErrorResponse {
	static <T> SuccessResponse<T> success(T pData) {
		return new SuccessResponse<>(pData);
	}
}