/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.battle.domain;

import com.mealam.showdown.battle.api.BattleRepository;
import com.mealam.showdown.battle.api.BattleService;
import com.mealam.showdown.battle.context.BattleContext;
import com.mealam.showdown.battle.context.DragonBattleContext;
import com.mealam.showdown.battle.context.PlayerBattleContext;
import com.mealam.showdown.battle.context.TurnContext;
import com.mealam.showdown.battle.context.TurnHistoryContext;
import com.mealam.showdown.battle.data.BattleId;
import com.mealam.showdown.battle.data.Phase;
import com.mealam.showdown.battle.data.TeamId;
import com.mealam.showdown.battle.data.TurnManager;
import com.mealam.showdown.battle.dto.request.CreateBattleRequest;
import com.mealam.showdown.battle.dto.request.JoinBattleRequest;
import com.mealam.showdown.battle.dto.request.LeaveBattleRequest;
import com.mealam.showdown.battle.dto.request.TurnBattleRequest;
import com.mealam.showdown.battle.dto.response.JoinBattleResponse;
import com.mealam.showdown.battle.party.PartyService;
import com.mealam.showdown.user.context.UserContext;
import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.utils.logging.BaseLogLevel;
import com.mealam.showdown.utils.logging.BaseLogger;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBattleService implements BattleService {

	private static final int MIN_TEAMS = 2;

	private final BattleRepository repo;
	private final PartyService partyService;
	private final Map<BattleId, TurnManager> turnManagers = new ConcurrentHashMap<>();
	private final Map<BattleId, Object> battleLocks = new ConcurrentHashMap<>();

	public DefaultBattleService(BattleRepository pRepository, PartyService pPartyService) {
		this.repo = Objects.requireNonNull(pRepository);
		this.partyService = Objects.requireNonNull(pPartyService);
	}

	private Object getBattleLock(BattleId pBattleId) {
		return battleLocks.computeIfAbsent(pBattleId, id -> new Object());
	}

	@Override
	public BattleContext createBattle(CreateBattleRequest pRequest) {
		BattleId battleId = BattleId.generate();
		TurnManager turnManager = new TurnManager();
		TurnContext initialTurn = turnManager.createBattle();

		turnManagers.put(battleId, turnManager);

		Map<TeamId, List<UserId>> teams = new LinkedHashMap<>();
		List<UserId> spectatorIds = new ArrayList<>();

		if (pRequest != null && pRequest.playerIds() != null && !pRequest.playerIds().isEmpty()) {
			TeamId defaultTeam = TeamId.generate();
			List<UserId> players = pRequest.playerIds().stream()
					.map(UserId::parse)
					.toList();
			teams.put(defaultTeam, players);
		}

		if (pRequest != null && pRequest.spectatorIds() != null) {
			spectatorIds.addAll(pRequest.spectatorIds().stream()
					.map(UserId::parse)
					.toList());
		}

		var battle = new BattleContext(
				battleId, teams, spectatorIds,
				initialTurn, Phase.REGISTRY.defaultVersion(), null,
				new ArrayList<>());

		repo.save(battle);
		return battle;
	}

	@Override
	public BattleContext startBattle(BattleId pBattleId) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		TurnManager turnManager = turnManagers.get(pBattleId);
		if (turnManager == null) {
			throw new IllegalStateException("Turn manager not found for battle");
		}

		TurnContext current = battle.turnContext();
		if (current != null && current.status() != TurnContext.TurnStatus.NOT_STARTED) {
			throw new IllegalStateException("Battle already started");
		}
		if (battle.teams() == null || battle.teams().isEmpty()) {
			throw new IllegalStateException("Cannot start battle: no teams exist");
		}
		if (battle.teams().size() < MIN_TEAMS) {
			throw new IllegalStateException("Cannot start battle: need at least " + MIN_TEAMS + " teams");
		}

		TeamId startingTeam = battle.teams().keySet().iterator().next();
		TurnContext startContext = turnManager.startBattle(startingTeam);

		var inProgress = Phase.REGISTRY.versions().get("in_progress");
		List<TurnHistoryContext> history = new ArrayList<>(battle.turnHistory());

		var updated = new BattleContext(
				battle.battleId(), battle.teams(), battle.spectatorIds(),
				startContext, inProgress, battle.winnerTeamId(),
				history);

		repo.update(updated);
		return updated;
	}

	@Override
	public BattleContext advanceTurn(BattleId pBattleId, UserId pActingUserId, TurnBattleRequest pTurnData) {
		BaseLogger.log(BaseLogLevel.INFO,
				"Service.advanceTurn → start battleId=" + pBattleId
						+ " actingUserId=" + pActingUserId
						+ " action=" + (pTurnData == null ? "null" : pTurnData.action()));

		var battle = repo.get(pBattleId);
		if (battle == null) {
			BaseLogger.log(BaseLogLevel.WARNING, "Service.advanceTurn → battle not found battleId=" + pBattleId);
			return null;
		}

		try {
			var ctx = battle.turnContext();

			if (ctx == null || ctx.status() == TurnContext.TurnStatus.NOT_STARTED) {
				throw new IllegalStateException("Battle not started");
			}
			if (ctx.status() == TurnContext.TurnStatus.FINISHED) {
				throw new IllegalStateException("Battle already finished");
			}
			if (pActingUserId == null) {
				throw new IllegalArgumentException("Acting user is required");
			}
			if (pTurnData == null || pTurnData.action() == null || pTurnData.action().isBlank()) {
				throw new IllegalArgumentException("Action is required");
			}

			TeamId playerTeam = battle.getTeamForPlayer(pActingUserId);
			if (playerTeam == null) {
				throw new IllegalArgumentException("Acting user is not a player in this battle");
			}

			TeamId activeTeam = ctx.activeTeamId();
			if (activeTeam == null || !activeTeam.equals(playerTeam)) {
				throw new IllegalArgumentException("Only players from the active team can act");
			}

			List<UserId> activeTeamPlayers = battle.teams().get(activeTeam);
			if (activeTeamPlayers == null || activeTeamPlayers.isEmpty()) {
				throw new IllegalStateException("Active team has no players");
			}

			Map<String, TurnContext.PlayerTurnSubmission> merged = new LinkedHashMap<>();
			if (ctx.submissions() != null) merged.putAll(ctx.submissions());

			String actingKey = pActingUserId.toString();
			if (merged.containsKey(actingKey)) {
				throw new IllegalArgumentException("Player has already acted this turn");
			}

			Instant submittedAt = Instant.now();
			merged.put(
					actingKey,
					new TurnContext.PlayerTurnSubmission(actingKey, pTurnData.action(), submittedAt));

			boolean activeTeamFinished = activeTeamPlayers.stream()
					.map(UserId::toString)
					.allMatch(merged::containsKey);

			List<TurnHistoryContext> history = new ArrayList<>(battle.turnHistory());
			List<TurnHistoryContext.TurnEvent> events = new ArrayList<>();
			events.add(new TurnHistoryContext.TurnEvent.ActionSubmitted(
					submittedAt, pActingUserId, playerTeam, pTurnData.action()));

			if (!activeTeamFinished) {
				TurnContext sameTurn = new TurnContext(
						ctx.turnNumber(),
						TurnContext.TurnStatus.IN_PROGRESS,
						activeTeam,
						Map.copyOf(merged));

				var updated = new BattleContext(
						battle.battleId(), battle.teams(), battle.spectatorIds(),
						sameTurn, battle.phase(), battle.winnerTeamId(),
						history);

				repo.update(updated);
				return updated;
			}

			Instant endedAt = Instant.now();
			events.add(new TurnHistoryContext.TurnEvent.TeamCompleted(endedAt, activeTeam));

			Map<String, String> finalActions = new LinkedHashMap<>();
			for (var e : merged.entrySet()) finalActions.put(e.getKey(), e.getValue().action());

			TeamId nextTurnOpeningTeam = getNextTeam(battle.teams(), activeTeam);

			TurnManager turnManager = turnManagers.get(pBattleId);
			if (turnManager == null) {
				throw new IllegalStateException("Turn manager not found for battle");
			}

			int fromTurn = ctx.turnNumber();
			TurnContext newTurn = turnManager.advance(nextTurnOpeningTeam);

			Instant advancedAt = Instant.now();
			events.add(new TurnHistoryContext.TurnEvent.TurnAdvanced(
					advancedAt, fromTurn, newTurn.turnNumber(), nextTurnOpeningTeam));

			Instant turnStartedAt = events.getFirst().at();

			history.add(new TurnHistoryContext(
					ctx.turnNumber(),
					turnStartedAt,
					endedAt,
					activeTeam,
					List.copyOf(events),
					new TurnHistoryContext.TurnResolution(Map.copyOf(finalActions))));

			var updated = new BattleContext(
					battle.battleId(), battle.teams(), battle.spectatorIds(),
					newTurn, battle.phase(), battle.winnerTeamId(),
					history);

			repo.update(updated);

			BaseLogger.log(BaseLogLevel.INFO,
					"Service.advanceTurn → advanced battleId=" + pBattleId
							+ " fromTurn=" + fromTurn
							+ " toTurn=" + newTurn.turnNumber()
							+ " nextActiveTeamId=" + newTurn.activeTeamId()
							+ " turnHistory=" + history.size());

			return updated;

		} catch (RuntimeException ex) {
			BaseLogger.log(BaseLogLevel.ERROR,
					"Service.advanceTurn → failed battleId=" + pBattleId
							+ " actingUserId=" + pActingUserId
							+ " action=" + (pTurnData == null ? "null" : pTurnData.action()),
					ex);
			throw ex;
		}
	}

	private TeamId getNextTeam(Map<TeamId, List<UserId>> pTeams, TeamId pCurrentTeam) {
		List<TeamId> teamIds = new ArrayList<>(pTeams.keySet());
		int currentIndex = teamIds.indexOf(pCurrentTeam);
		if (currentIndex < 0) return teamIds.getFirst();
		return teamIds.get((currentIndex + 1) % teamIds.size());
	}

	@Override
	public BattleContext finishBattle(BattleId pBattleId, UserId pWinnerId) {
		var battle = repo.get(pBattleId);
		if (battle == null) return null;

		TurnManager tm = turnManagers.get(pBattleId);
		TurnContext finishedCtx = (tm != null) ? tm.finish()
				: new TurnContext(
						-1, TurnContext.TurnStatus.FINISHED, null, null);

		TeamId winnerTeam = pWinnerId != null ? battle.getTeamForPlayer(pWinnerId) : null;

		List<TurnHistoryContext> history = new ArrayList<>(battle.turnHistory());
		history.add(new TurnHistoryContext(
				finishedCtx.turnNumber(),
				Instant.now(),
				Instant.now(),
				null,
				List.of(new TurnHistoryContext.TurnEvent.BattleFinished(Instant.now(), winnerTeam)),
				null));

		var updated = new BattleContext(
				battle.battleId(), battle.teams(), battle.spectatorIds(),
				finishedCtx, battle.phase(), winnerTeam,
				history);

		repo.update(updated);
		turnManagers.remove(pBattleId);
		return updated;
	}

	@Override
	public BattleContext getBattle(BattleId pBattleId) {
		return repo.get(pBattleId);
	}

	@Override
	public JoinBattleResponse joinBattle(BattleId pBattleId, JoinBattleRequest pRequest) {
		synchronized (getBattleLock(pBattleId)) {
			var battle = repo.get(pBattleId);
			if (battle == null) return null;

			if (pRequest.userId() == null || pRequest.userId().isBlank()) {
				throw new IllegalArgumentException("userId is required");
			}

			UserId userId = UserId.parse(pRequest.userId());
			UserContext profileContext = new UserContext(
					userId, userId.toString());

			if (battle.getAllPlayerIds().contains(userId)) {
				return buildPlayerJoinResponse(pBattleId, profileContext, userId);
			}

			Map<TeamId, List<UserId>> mutableTeams = new LinkedHashMap<>();
			battle.teams().forEach((teamId, players) -> mutableTeams.put(teamId, new ArrayList<>(players)));

			List<UserId> spectators = new ArrayList<>(battle.spectatorIds());
			spectators.remove(userId);

			TeamId targetTeam = pRequest.parseTeamId();

			if (targetTeam != null) {
				if (!mutableTeams.containsKey(targetTeam)) {
					mutableTeams.put(targetTeam, new ArrayList<>());
				}
				mutableTeams.get(targetTeam).add(userId);
			} else {
				TeamId smallestTeam = findSmallestTeam(mutableTeams);
				if (smallestTeam == null) {
					smallestTeam = TeamId.generate();
					mutableTeams.put(smallestTeam, new ArrayList<>());
				}
				mutableTeams.get(smallestTeam).add(userId);
			}

			var updated = new BattleContext(
					battle.battleId(), mutableTeams, spectators,
					battle.turnContext(), battle.phase(), battle.winnerTeamId(),
					battle.turnHistory());

			repo.update(updated);
			return buildPlayerJoinResponse(pBattleId, profileContext, userId);
		}
	}

	private TeamId findSmallestTeam(Map<TeamId, List<UserId>> pTeams) {
		if (pTeams.isEmpty()) return null;
		return pTeams.entrySet().stream()
				.min(Comparator.comparingInt(e -> e.getValue().size()))
				.map(Map.Entry::getKey)
				.orElse(null);
	}

	@Override
	public BattleContext leaveBattle(BattleId pBattleId, LeaveBattleRequest pRequest) {
		synchronized (getBattleLock(pBattleId)) {
			var battle = repo.get(pBattleId);
			if (battle == null) return null;

			if (pRequest.userId() == null || pRequest.userId().isBlank()) {
				throw new IllegalArgumentException("userId is required");
			}

			UserId userId = UserId.parse(pRequest.userId());

			Map<TeamId, List<UserId>> mutableTeams = new LinkedHashMap<>();
			boolean removed = false;
			for (Map.Entry<TeamId, List<UserId>> entry : battle.teams().entrySet()) {
				List<UserId> players = new ArrayList<>(entry.getValue());
				if (players.remove(userId)) {
					removed = true;
				}
				if (!players.isEmpty()) {
					mutableTeams.put(entry.getKey(), players);
				}
			}

			List<UserId> spectators = new ArrayList<>(battle.spectatorIds());
			removed |= spectators.remove(userId);

			if (!removed) return battle;

			var updated = new BattleContext(
					battle.battleId(), mutableTeams, spectators,
					battle.turnContext(), battle.phase(), battle.winnerTeamId(),
					battle.turnHistory());

			repo.update(updated);
			return updated;
		}
	}

	private JoinBattleResponse.Player buildPlayerJoinResponse(BattleId pBattleId, UserContext pContext, UserId pUserId) {
		List<DragonBattleContext> party = partyService.getUserParty(pUserId);
		party = partyService.preparePartyForBattle(party);
		var playerCtx = new PlayerBattleContext(pContext, party);
		return new JoinBattleResponse.Player(pBattleId, playerCtx);
	}
}
