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
