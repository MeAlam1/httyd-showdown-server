package com.mealam.showdown.teambuilder.domain;

import com.mealam.showdown.teambuilder.api.TeamBuilderService;
import com.mealam.showdown.teambuilder.context.TeamContext;
import com.mealam.showdown.teambuilder.data.TeamId;
import com.mealam.showdown.teambuilder.dto.request.CreateTeamRequest;
import com.mealam.showdown.teambuilder.dto.request.UpdateTeamRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultTeamBuilderService implements TeamBuilderService {

	private final Map<TeamId, TeamContext> store = new ConcurrentHashMap<>();

	@Override
	public TeamContext createTeam(CreateTeamRequest pRequest) {
		if (pRequest == null) throw new IllegalArgumentException("Request is required");
		if (pRequest.ownerId() == null || pRequest.ownerId().isBlank()) throw new IllegalArgumentException("ownerId is required");
		if (pRequest.name() == null || pRequest.name().isBlank()) throw new IllegalArgumentException("name is required");

		TeamId id = TeamId.generate();
		var ctx = new TeamContext(id, pRequest.ownerId(), pRequest.name(), pRequest.dragonIds());
		store.put(id, ctx);
		return ctx;
	}

	@Override
	public TeamContext getTeam(TeamId pTeamId) {
		return store.get(pTeamId);
	}

	@Override
	public List<TeamContext> listTeams(String pOwnerId) {
		if (pOwnerId == null || pOwnerId.isBlank()) {
			return store.values().stream().toList();
		}
		return store.values().stream()
				.filter(t -> Objects.equals(t.ownerId(), pOwnerId))
				.collect(Collectors.toList());
	}

	@Override
	public TeamContext updateTeam(TeamId pTeamId, UpdateTeamRequest pRequest) {
		if (pTeamId == null) throw new IllegalArgumentException("teamId is required");
		if (pRequest == null) throw new IllegalArgumentException("Request is required");

		TeamContext existing = store.get(pTeamId);
		if (existing == null) return null;

		String name = pRequest.name() != null ? pRequest.name() : existing.name();
		var dragonIds = pRequest.dragonIds() != null ? pRequest.dragonIds() : existing.dragonIds();

		var updated = new TeamContext(existing.teamId(), existing.ownerId(), name, dragonIds);
		store.put(pTeamId, updated);
		return updated;
	}

	@Override
	public boolean deleteTeam(TeamId pTeamId) {
		return store.remove(pTeamId) != null;
	}
}