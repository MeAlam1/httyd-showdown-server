package com.mealam.showdown.db.controller;

import com.mealam.showdown.db.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService pUserService) {
		this.userService = pUserService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, String> pBody) {
		try {
			userService.register(pBody.get("username"), pBody.get("password"));
			return ResponseEntity.ok("Registered successfully!");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/* TODO: Testing */
	@GetMapping("/register")
	public ResponseEntity<String> register(
			@RequestParam String pUsername,
			@RequestParam String pPassword) {
		try {
			userService.register(pUsername, pPassword);
			return ResponseEntity.ok("Registered successfully!");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> pBody) {
		boolean success = userService.login(pBody.get("username"), pBody.get("password"));
		return success ? ResponseEntity.ok("Login successful!") :
				ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}
}
