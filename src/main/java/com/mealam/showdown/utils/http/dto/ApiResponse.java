/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.utils.http.dto;

public sealed interface ApiResponse<T> permits SuccessResponse, ErrorResponse {

	static <T> SuccessResponse<T> success(T pData) {
		return new SuccessResponse<>(pData);
	}
}
