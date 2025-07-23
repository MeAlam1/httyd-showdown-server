/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.user.context;

import com.mealam.showdown.user.data.UserId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

	private final Map<Long, UserContext> userContexts = new ConcurrentHashMap<>();

	public UserContext createContext(Long pId, UserId pUserId, String pUsername) {
		UserContext context = new UserContext(pId, pUserId, pUsername);
		userContexts.put(pId, context);
		return context;
	}

	public UserContext getContext(Long pUserId) {
		return userContexts.get(pUserId);
	}

	public void removeContext(Long pUserId) {
		userContexts.remove(pUserId);
	}
}
