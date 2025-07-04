package com.mealam.showdown.db.user;

import com.mealam.showdown.users.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final UserContextService userContextService;

	public AuthController(UserService pUserService, UserContextService pUserContextService) {
		this.userService = pUserService;
		this.userContextService = pUserContextService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, String> pBody) {
		try {
			UUID userId = userService.register(pBody.get("username"), pBody.get("password")).getId();
			userContextService.createContext(userId, pBody.get("username"));
			return ResponseEntity.ok("Registered successfully!");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> pBody) {
		boolean success = userService.login(pBody.get("username"), pBody.get("password"));
		if (success) {
			UUID userId = userService.getUserIdByUsername(pBody.get("username"));
			if (userContextService.getContext(userId) == null) {
				userContextService.createContext(userId, pBody.get("username"));
			}
			return ResponseEntity.ok("Login successful!");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}
}