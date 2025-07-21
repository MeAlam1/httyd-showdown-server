package com.mealam.showdown.user;

import com.mealam.showdown.user.context.UserContextService;
import com.mealam.showdown.security.CaptchaService;
import com.mealam.showdown.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final UserContextService userContextService;
	private final JwtUtil jwtUtil;
	private final CaptchaService captchaService;

	public AuthController(UserService pUserService, UserContextService pUserContextService, JwtUtil pJwtUtil, CaptchaService pCaptchaService) {
		this.userService = pUserService;
		this.userContextService = pUserContextService;
		this.jwtUtil = pJwtUtil;
		this.captchaService = pCaptchaService;
	}

	@GetMapping("/captcha")
	public Map<String, String> getCaptcha() {
		return captchaService.generateCaptcha();
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, String> pBody, HttpServletResponse pResponse) {
		String captchaId = pBody.get("captchaId");
		String captchaAnswer = pBody.get("captchaAnswer");
		if (captchaId == null || captchaAnswer == null || !captchaService.verify(captchaId, captchaAnswer)) {
			return ResponseEntity.badRequest().body("Invalid CAPTCHA");
		}
		try {
			User user = userService.register(pBody.get("username"), pBody.get("password"));
			if (user == null) {
				return ResponseEntity.badRequest().body("User already exists.");
			}
			userContextService.createContext(user.getId(), pBody.get("username"));
			String token = jwtUtil.generateToken(user.getId(), user.getUsername());

			Cookie cookie = new Cookie("jwt", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge((int) (jwtUtil.getExpirationMs() / 1000));
			pResponse.addCookie(cookie);

			return ResponseEntity.ok("Registration successful");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> pBody, HttpServletResponse pResponse) {
		String captchaId = pBody.get("captchaId");
		String captchaAnswer = pBody.get("captchaAnswer");
		if (captchaId == null || captchaAnswer == null || !captchaService.verify(captchaId, captchaAnswer)) {
			return ResponseEntity.badRequest().body("Invalid CAPTCHA");
		}
		boolean success = userService.login(pBody.get("username"), pBody.get("password"));
		if (success) {
			Optional<Long> userIdOpt = userService.getUserIdByUsername(pBody.get("username"));
			if (userIdOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
			}
			Long userId = userIdOpt.get();
			if (userContextService.getContext(userId) == null) {
				userContextService.createContext(userId, pBody.get("username"));
			}
			String token = jwtUtil.generateToken(userId, pBody.get("username"));

			Cookie cookie = new Cookie("jwt", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge((int) (jwtUtil.getExpirationMs() / 1000));
			pResponse.addCookie(cookie);

			return ResponseEntity.ok("Login successful");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}
}
