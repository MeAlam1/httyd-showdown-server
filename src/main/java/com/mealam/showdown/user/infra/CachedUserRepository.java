package com.mealam.showdown.user.infra;

import com.mealam.showdown.user.api.UserRepository;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.user.model.UserAccount;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CachedUserRepository implements UserRepository {

	private final UserRepository delegate;
	private final Map<String, UserAccount> cacheById = new ConcurrentHashMap<>();

	public CachedUserRepository(UserRepository pDelegate) {
		this.delegate = Objects.requireNonNull(pDelegate);
	}

	@Override
	public Optional<UserAccount> findById(UserId pUserId) {
		if (pUserId == null) return Optional.empty();

		UserAccount cached = cacheById.get(pUserId.toString());
		if (cached != null) return Optional.of(cached);

		Optional<UserAccount> loaded = delegate.findById(pUserId);
		loaded.ifPresent(a -> cacheById.put(a.id().toString(), a));
		return loaded;
	}

	@Override
	public Optional<UserAccount> findByUsername(String pUsername) {
		Optional<UserAccount> loaded = delegate.findByUsername(pUsername);
		loaded.ifPresent(a -> cacheById.put(a.id().toString(), a));
		return loaded;
	}

	@Override
	public UserAccount save(UserAccount pAccount) {
		UserAccount saved = delegate.save(pAccount);
		cacheById.put(saved.id().toString(), saved);
		return saved;
	}

	@Override
	public List<UserAccount> findAll() {
		List<UserAccount> all = delegate.findAll();
		all.forEach(a -> cacheById.put(a.id().toString(), a));
		return all;
	}

	public void invalidate(UserId pUserId) {
		if (pUserId != null) cacheById.remove(pUserId.toString());
	}

	public void clear() {
		cacheById.clear();
	}
}