package com.mealam.showdown.user.http;

import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.user.domain.UserService;
import com.mealam.showdown.utils.http.ResponseUtils;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import io.javalin.http.Context;

import java.util.Objects;

public final class UserController {

	private final UserService service;

	public UserController(UserService pService) {
		this.service = Objects.requireNonNull(pService);
	}

	public void createUser(Context pContext) {
		try {
			CreateUserRequest req = pContext.bodyAsClass(CreateUserRequest.class);
			if (req == null || req.username() == null || req.username().isBlank()) {
				ResponseUtils.badRequest(pContext, "Username is required", "username_required");
				return;
			}

			var created = service.createUser(req.username().trim());
			ResponseUtils.created(pContext, new UserResponse(created.id().toString(), created.username()));
		} catch (IllegalArgumentException ex) {
			ResponseUtils.badRequest(pContext, ex.getMessage(), "user_create_invalid");
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error creating user", ex);
			ResponseUtils.serverError(pContext, "Failed to create user", "user_create_failed");
		}
	}

	public void getProfile(Context pContext) {
		try {
			UserId id = UserId.parse(pContext.pathParam("id"));
			var profile = service.getProfile(id);

			if (profile == null || profile.user() == null) {
				ResponseUtils.notFound(pContext, "User not found", "user_not_found");
				return;
			}

			var u = profile.user();
			ResponseUtils.ok(pContext, new UserResponse(u.userId().toString(), u.username()));
		} catch (IllegalArgumentException ex) {
			ResponseUtils.badRequest(pContext, "Invalid user ID", "invalid_user_id");
		} catch (Exception ex) {
			BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving user profile", ex);
			ResponseUtils.serverError(pContext, "Internal server error", "user_get_error");
		}
	}

	public record CreateUserRequest(String username) {
	}

	public record UserResponse(String id, String username) {
	}
}