package com.mealam.showdown.user.domain;

import com.mealam.showdown.user.api.UserRepository;
import com.mealam.showdown.user.context.UserContext;
import com.mealam.showdown.user.context.UserProfileContext;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.user.model.UserAccount;

import java.util.Objects;
import java.util.Optional;

public final class UserService {

	private final UserRepository users;

	public UserService(UserRepository pUsers) {
		this.users = Objects.requireNonNull(pUsers);
	}

	public UserProfileContext getProfile(UserId pUserId) {
		Optional<UserAccount> account = users.findById(pUserId);
		return account
				.map(a -> new UserProfileContext(new UserContext(a.id(), a.username())))
				.orElseGet(() -> new UserProfileContext(null));
	}

	public UserAccount createUser(String pUsername) {
		Objects.requireNonNull(pUsername);

		users.findByUsername(pUsername).ifPresent(existing -> {
			throw new IllegalArgumentException("Username already exists");
		});

		UserAccount account = new UserAccount(UserId.generate(), pUsername);
		return users.save(account);
	}
}