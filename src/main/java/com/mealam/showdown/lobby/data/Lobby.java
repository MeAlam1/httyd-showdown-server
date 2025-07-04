package com.mealam.showdown.lobby.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Lobby(
		String id,
		String host,
		List<String> players,
		List<String> spectators,
		int maxPlayers) {

	public static Lobby create(String pId, String pHost, int pMaxPlayers) {
		List<String> players = new ArrayList<>();
		players.add(pHost);
		return new Lobby(pId, pHost, Collections.unmodifiableList(players), Collections.emptyList(), pMaxPlayers);
	}

	public Lobby addPlayer(String pPlayer) {
		if (!isFull()) {
			List<String> newPlayers = new ArrayList<>(players);
			newPlayers.add(pPlayer);
			return new Lobby(id, host, Collections.unmodifiableList(newPlayers), spectators, maxPlayers);
		} else {
			List<String> newSpectators = new ArrayList<>(spectators);
			newSpectators.add(pPlayer);
			return new Lobby(id, host, players, Collections.unmodifiableList(newSpectators), maxPlayers);
		}
	}

	public boolean isSpectator(String pPlayer) {
		return spectators.contains(pPlayer);
	}

	public boolean isFull() {
		return players.size() >= maxPlayers;
	}
}
