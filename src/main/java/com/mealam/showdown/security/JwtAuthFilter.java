/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthFilter(JwtUtil pJwtUtil) {
		this.jwtUtil = pJwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest pRequest, HttpServletResponse pResponse, FilterChain pFilterChain)
			throws ServletException, IOException {
		String authHeader = pRequest.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				Jws<Claims> claims = jwtUtil.validateToken(token);
				String userId = claims.getPayload().getSubject();
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(pRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception ignored) {}
		}
		pFilterChain.doFilter(pRequest, pResponse);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest pRequest) {
		String path = pRequest.getServletPath();
		return path.equals("/auth/login") || path.equals("/auth/register")
				|| path.equals("/auth/captcha");
	}
}
