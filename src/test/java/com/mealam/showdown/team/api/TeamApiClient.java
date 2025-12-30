package com.mealam.showdown.team.api;

import com.mealam.showdown.team.utils.TeamTestUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TeamApiClient {

	private final int port;
	private final HttpClient client;

	public TeamApiClient(int pPort, HttpClient pClient) {
		this.port = pPort;
		this.client = pClient;
	}

	public String baseUrl() {
		return "http://localhost:" + port;
	}

	public HttpResponse<String> createTeam(String pJsonBody) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/team/create"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(pJsonBody == null ? "{}" : pJsonBody))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> getTeam(String pTeamId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/team/" + pTeamId))
				.GET()
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<String> deleteTeam(String pTeamId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl() + "/api/team/" + pTeamId + "/delete"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	public String createTeamAndGetId(String pName) throws Exception {
		String body = "{\"name\":\"" + (pName == null ? "" : pName) + "\",\"dragons\":[]}";
		HttpResponse<String> res = createTeam(body);
		return TeamTestUtils.extractTeamIdFromBody(res.body());
	}
}