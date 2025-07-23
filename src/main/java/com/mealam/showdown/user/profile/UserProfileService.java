/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.user.profile;

import com.mealam.showdown.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

	private final UserProfileRepository userRepository;

	public UserProfileService(UserProfileRepository pUserRepository) {
		this.userRepository = pUserRepository;
	}

	public void createUserProfile(User pUser) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUser(pUser);
		userRepository.save(userProfile);
	}
}
