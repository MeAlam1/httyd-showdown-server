package com.mealam.showdown.user.api;

import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.user.model.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	Optional<UserAccount> findById(UserId pUserId);

	Optional<UserAccount> findByUsername(String pUsername);

	UserAccount save(UserAccount pAccount);

	List<UserAccount> findAll();
}