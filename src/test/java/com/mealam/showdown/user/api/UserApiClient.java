package com.mealam.showdown.user.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserApiClient {

	private final int port;
	private final HttpClient client;

	public UserApiClient(int pPort, HttpClient pClient) {
		this.port = pPort;
		this.client = pClient;
	}

	public String baseUrl() {
		return "http://localhost:" + port;
	}

	public HttpResponse<String> createUser(String pUsername) throws Exception {
		String body = pUsername == null
				? "{}"
				: "{\"username\":\"" + escapeJson(pUsername) + "\"}";

		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/user"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build();

		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> getUser(String pUserId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/user/" + pUserId))
				.GET()
				.build();

		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	private static String escapeJson(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}