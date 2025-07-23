/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity pHttp, JwtAuthFilter pJwtAuthFilter) throws Exception {
		pHttp
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.anyRequest().permitAll());
		/*.requestMatchers(
				"/auth/login",
				"/auth/register",
				"/auth/captcha",
				"/test-kt",
				"/error",
				"/actuator",
				"/actuator/health",
				"/actuator/health/**",
				"/webjars/**",
				"/api/uuid/**")
		.permitAll()
		.anyRequest().authenticated())
		.addFilterBefore(pJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);*/
		return pHttp.build();
	}
}
