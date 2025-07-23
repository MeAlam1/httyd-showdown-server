package com.mealam.showdown.user.context;

import com.mealam.showdown.user.User;
import com.mealam.showdown.user.UserRepository;
import com.mealam.showdown.user.data.UserId;
import org.springframework.stereotype.Service;

@Service
public class UserProfileContextService {

	private final UserRepository userRepository;

	public UserProfileContextService(UserRepository pUserRepository) {
		this.userRepository = pUserRepository;
	}

	public UserProfileContext getByUserId(UserId pUserId) {
		User user = userRepository.findByUserId(pUserId.value()).orElse(null);
		if (user == null) return null;
		UserContext userContext = new UserContext(user.getId(), pUserId, user.getUsername());
		return new UserProfileContext(userContext);
	}
}
