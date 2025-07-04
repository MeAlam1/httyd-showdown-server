package com.mealam.showdown.db.service;

import com.mealam.showdown.db.model.User;
import com.mealam.showdown.db.repository.UserRepository;
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
			throw new RuntimeException("Username already exists");
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
}
