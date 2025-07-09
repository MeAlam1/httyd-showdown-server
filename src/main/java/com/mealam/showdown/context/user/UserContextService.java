package com.mealam.showdown.context.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

	private final Map<Long, UserContext> userContexts = new ConcurrentHashMap<>();

	public UserContext createContext(Long pUserId, String pUsername) {
		UserContext context = new UserContext(pUserId, pUsername);
		userContexts.put(pUserId, context);
		return context;
	}

	public UserContext getContext(Long pUserId) {
		return userContexts.get(pUserId);
	}

	public void removeContext(Long pUserId) {
		userContexts.remove(pUserId);
	}
}
