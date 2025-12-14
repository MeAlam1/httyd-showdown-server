package com.mealam.showdown.utils.http.dto;

public record ErrorResponse(String error, String code) implements ApiResponse<Void> {
	public static ErrorResponse of(String pError, String pCode) {
		return new ErrorResponse(pError, pCode);
	}
}