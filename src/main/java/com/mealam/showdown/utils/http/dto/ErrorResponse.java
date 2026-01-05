/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.http.dto;

public record ErrorResponse(String error, String code) implements ApiResponse<Void> {

	public static ErrorResponse of(String pError, String pCode) {
		return new ErrorResponse(pError, pCode);
	}
}
