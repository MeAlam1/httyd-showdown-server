package com.mealam.showdown.context.user;

import java.util.UUID;

public record UserContext(
		UUID userId,
		String username
) {
}