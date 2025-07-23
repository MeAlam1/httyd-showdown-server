package com.mealam.showdown.user.context;

import org.jetbrains.annotations.Nullable;

public record UserProfileContext(
		@Nullable UserContext user) {}
