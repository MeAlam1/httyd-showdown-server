package com.mealam.showdown.user.model;

import com.mealam.showdown.user.data.UserId;

public record UserAccount(
		UserId id,
		String username
) {
}