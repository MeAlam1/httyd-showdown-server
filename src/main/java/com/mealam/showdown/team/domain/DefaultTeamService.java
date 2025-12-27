package com.mealam.showdown.team.domain;

import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.team.api.TeamRepository;
import com.mealam.showdown.team.api.TeamService;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.team.dto.request.CreateTeamRequest;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultTeamService implements TeamService {

	private static final int MAX_TEAM_SIZE = 6;

	private final TeamRepository repo;

	public DefaultTeamService(TeamRepository pRepo) {
		this.repo = Objects.requireNonNull(pRepo, "PartyRepository is required");
	}

	@Override
	public TeamContext createTeam(CreateTeamRequest pRequest) {
		if (pRequest == null) throw new IllegalArgumentException("Request is required");
		if (pRequest.name() == null || pRequest.name().isBlank())
			throw new IllegalArgumentException("name is required");

		Set<String> normalizedIds = normalizeDragonIds(pRequest.dragonIds());
		if (normalizedIds.size() > MAX_TEAM_SIZE) {
			throw new IllegalArgumentException("team size must be <= " + MAX_TEAM_SIZE);
		}

		validateDragonIdsExist(normalizedIds);

		TeamId id = TeamId.generate();
		var ctx = new TeamContext(id, pRequest.name(), new ArrayList<>(normalizedIds));
		return repo.save(ctx);
	}

	@Override
	public TeamContext getTeam(TeamId pTeamId) {
		return repo.get(pTeamId);
	}

	@Override
	public boolean deleteTeam(TeamId pTeamId) {
		return repo.delete(pTeamId);
	}

	private static Set<String> normalizeDragonIds(java.util.List<String> pDragonIds) {
		Set<String> out = new LinkedHashSet<>();
		if (pDragonIds == null) return out;

		for (String id : pDragonIds) {
			if (id == null) continue;
			String trimmed = id.trim();
			if (trimmed.isEmpty()) continue;
			out.add(trimmed);
		}

		return out;
	}

	private static void validateDragonIdsExist(Set<String> pDragonIds) {
		Map<String, ?> dragons = ResourceCache.getDragons();
		if (dragons == null || dragons.isEmpty()) {
			throw new IllegalStateException("Dragon cache is not loaded");
		}

		for (String dragonId : pDragonIds) {
			if (!dragons.containsKey(dragonId)) {
				throw new IllegalArgumentException("Unknown dragonId: " + dragonId);
			}
		}
	}
}