package com.mealam.showdown.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
						.requestMatchers(
								"/auth/login",
								"/auth/register",
								"/test-kt",
								"/error",
								"/actuator",
								"/actuator/health",
								"/actuator/health/**",
								"/webjars/**"
						).permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(pJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return pHttp.build();
	}
}
