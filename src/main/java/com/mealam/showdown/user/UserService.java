package com.mealam.showdown.user;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository pUserRepository, PasswordEncoder pPasswordEncoder) {
		this.userRepository = pUserRepository;
		this.passwordEncoder = pPasswordEncoder;
	}

	public User register(String pUsername, String pPassword) {
		if (userRepository.findByUsername(pUsername).isPresent()) {
			System.out.println("Warning: User already exists. Please login instead.");
			return null;
		}
		User user = new User();
		user.setUsername(pUsername);
		user.setPassword(passwordEncoder.encode(pPassword));
		return userRepository.save(user);
	}

	public boolean login(String pUsername, String pRawPassword) {
		return userRepository.findByUsername(pUsername)
				.map(user -> passwordEncoder.matches(pRawPassword, user.getPassword()))
				.orElse(false);
	}

	public Optional<Long> getUserIdByUsername(String pUsername) {
		Optional<User> userOpt = userRepository.findByUsername(pUsername);
		if (userOpt.isEmpty()) {
			System.out.println("Warning: User not found for username: " + pUsername);
		}
		return userOpt.map(User::getId);
	}
}
