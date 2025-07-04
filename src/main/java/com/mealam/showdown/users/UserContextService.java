package com.mealam.showdown.users;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserContextService {
	private final Map<UUID, UserContext> userContexts = new ConcurrentHashMap<>();

	public UserContext createContext(UUID pUserId, String pUsername) {
		UserContext context = new UserContext(pUserId, pUsername);
		userContexts.put(pUserId, context);
		return context;
	}

	public UserContext getContext(UUID pUserId) {
		return userContexts.get(pUserId);
	}

	public void removeContext(UUID pUserId) {
		userContexts.remove(pUserId);
	}
}