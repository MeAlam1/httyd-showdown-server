/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.error;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
public class ErrorControllerImpl implements ErrorController {

	private final ErrorAttributes errorAttributes;

	public ErrorControllerImpl(ErrorAttributes pErrorAttributes) {
		this.errorAttributes = pErrorAttributes;
	}

	@RequestMapping("/error")
	public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest pRequest) {
		ServletWebRequest webRequest = new ServletWebRequest(pRequest);
		Map<String, Object> body = errorAttributes.getErrorAttributes(
				webRequest, ErrorAttributeOptions.defaults());
		int status = (int) body.getOrDefault("status", 500);
		return ResponseEntity.status(status).body(body);
	}
}
