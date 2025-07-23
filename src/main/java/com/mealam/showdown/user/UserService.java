/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.user;

import com.mealam.showdown.user.profile.UserProfileService;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserProfileService userProfileService;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository pUserRepository, UserProfileService pUserProfileService, PasswordEncoder pPasswordEncoder) {
		this.userRepository = pUserRepository;
		this.userProfileService = pUserProfileService;
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
		User savedUser = userRepository.save(user);
		userProfileService.createUserProfile(savedUser);
		return savedUser;
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
