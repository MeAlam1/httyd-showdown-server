package com.mealam.showdown.user;

import com.mealam.showdown.user.api.UserBaseTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateTest extends UserBaseTest {

	private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");

	@Test
	void createUser_andGetProfile() throws Exception {
		HttpResponse<String> createRes = api.createUser("alpha");
		assertEquals(201, createRes.statusCode());
		assertNotNull(createRes.body());

		String userId = extractId(createRes.body());
		assertNotNull(userId);
		assertFalse(userId.isBlank());

		HttpResponse<String> getRes = api.getUser(userId);
		assertEquals(200, getRes.statusCode());
		assertNotNull(getRes.body());
		assertTrue(getRes.body().contains("\"id\""));
		assertTrue(getRes.body().contains("\"username\""));
		assertTrue(getRes.body().contains("alpha"));
	}

	@Test
	void createUser_duplicateUsername_returnsBadRequest() throws Exception {
		assertEquals(201, api.createUser("alpha").statusCode());

		HttpResponse<String> dup = api.createUser("alpha");
		assertEquals(400, dup.statusCode());
		assertNotNull(dup.body());
		assertTrue(dup.body().toLowerCase().contains("username"));
	}

	@Test
	void getProfile_unknownUser_returnsNotFound() throws Exception {
		HttpResponse<String> res = api.getUser("does_not_exist");
		assertEquals(404, res.statusCode());
	}

	private static String extractId(String body) {
		Matcher m = ID_PATTERN.matcher(body);
		return m.find() ? m.group(1) : null;
	}
}