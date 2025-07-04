package com.mealam.showdown.users;

import java.util.UUID;

public record UserContext(
		UUID userId,
		String username
) {
}