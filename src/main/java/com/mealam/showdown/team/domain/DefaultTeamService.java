/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.team.domain;

import com.mealam.showdown.loader.cache.ResourceCache;
import com.mealam.showdown.team.api.TeamRepository;
import com.mealam.showdown.team.api.TeamService;
import com.mealam.showdown.team.context.DragonContext;
import com.mealam.showdown.team.context.TeamContext;
import com.mealam.showdown.team.data.TeamId;
import com.mealam.showdown.team.dto.request.CreateDragonRequest;
import com.mealam.showdown.team.dto.request.CreateTeamRequest;
import com.mealam.showdown.team.factory.DragonContextBuilder;
import com.mealam.showdown.team.factory.TeamContextBuilder;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultTeamService implements TeamService {

	private static final int MAX_TEAM_SIZE = 6;

	private final TeamRepository repo;

	public DefaultTeamService(TeamRepository pRepo) {
		this.repo = Objects.requireNonNull(pRepo, "TeamRepository is required");
	}

	@Override
	public TeamContext createTeam(CreateTeamRequest pRequest) {
		if (pRequest == null) throw new IllegalArgumentException("Request is required");
		if (pRequest.name() == null || pRequest.name().isBlank())
			throw new IllegalArgumentException("name is required");

		String trimmedName = pRequest.name().trim();

		List<CreateDragonRequest> dragonRequests = pRequest.dragons() == null
				? List.of()
				: pRequest.dragons().stream().filter(Objects::nonNull).toList();

		if (dragonRequests.size() > MAX_TEAM_SIZE) {
			throw new IllegalArgumentException("team size must be <= " + MAX_TEAM_SIZE);
		}

		Set<String> normalizedIds = normalizeDragonIds(
				dragonRequests.stream().map(CreateDragonRequest::id).toList());
		validateDragonIdsExist(normalizedIds);

		TeamId id = TeamId.generate();

		List<DragonContext> dragons = dragonRequests.stream()
				.map(this::buildFullDragon)
				.toList();

		TeamContextBuilder builder = TeamContextBuilder.builder()
				.teamId(id)
				.name(trimmedName);

		dragons.forEach(builder::addDragon);

		TeamContext ctx = builder.build();

		return repo.save(ctx);
	}

	private DragonContext buildFullDragon(CreateDragonRequest req) {
		DragonContextBuilder builder = DragonContextBuilder.builder()
				.id(req.id());

		if (req.nickname() != null) builder.nickname(req.nickname());
		if (req.natureId() != null) builder.nature(req.natureId());
		if (req.level() != null) builder.level(req.level());
		if (req.abilityId() != null) builder.ability(req.abilityId());
		if (req.stats() != null) {
			var s = req.stats();
			builder.stats(
					s.attack() != null ? s.attack() : 0,
					s.speed() != null ? s.speed() : 0,
					s.defense() != null ? s.defense() : 0,
					s.armor() != null ? s.armor() : 0,
					s.firepower() != null ? s.firepower() : 0,
					s.stealth() != null ? s.stealth() : 0,
					s.stamina() != null ? s.stamina() : 0,
					s.shotLimit() != null ? s.shotLimit() : 0,
					s.venom() != null ? s.venom() : 0,
					s.jawStrength() != null ? s.jawStrength() : 0);
		}
		if (req.heldItemId() != null) builder.heldItem(req.heldItemId());
		if (req.moves() != null) {
			for (var move : req.moves()) {
				if (move != null && move.moveId() != null && move.slot() != null) {
					builder.addMove(move.moveId(), move.slot());
				}
			}
		}
		if (req.trainingEffort() != null) builder.trainingEffort(req.trainingEffort());

		return builder.build();
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

		Set<String> allowed = new LinkedHashSet<>();
		for (String key : dragons.keySet()) {
			if (key == null) continue;
			String k = key.trim();
			if (k.isEmpty()) continue;

			allowed.add(k);
			allowed.add(k.toLowerCase(Locale.ROOT));

			int slash = k.lastIndexOf('/');
			if (slash >= 0 && slash + 1 < k.length()) {
				String shortId = k.substring(slash + 1).trim();
				if (!shortId.isEmpty()) {
					allowed.add(shortId);
					allowed.add(shortId.toLowerCase(Locale.ROOT));
				}
			}
		}

		for (String dragonId : pDragonIds) {
			if (dragonId == null) continue;
			String raw = dragonId.trim();
			if (raw.isEmpty()) continue;

			String lower = raw.toLowerCase(Locale.ROOT);
			if (!allowed.contains(raw) && !allowed.contains(lower)) {
				throw new IllegalArgumentException("Unknown dragonId: " + dragonId);
			}
		}
	}

	private DragonContext buildMinimalDragon(String id) {
		return DragonContextBuilder.builder()
				.id(id)
				.build();
	}
}
