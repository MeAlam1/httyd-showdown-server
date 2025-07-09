package com.mealam.showdown.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private final SecretKey key = Jwts.SIG.HS256.key().build();
	@Getter
	private final long expirationMs = 86400000; // 1 day

	public String generateToken(Long pUserId, String pUsername) {
		return Jwts.builder()
				.subject(pUserId.toString())
				.claim("username", pUsername)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key)
				.compact();
	}

	public Jws<Claims> validateToken(String pToken) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(pToken);
	}
}
