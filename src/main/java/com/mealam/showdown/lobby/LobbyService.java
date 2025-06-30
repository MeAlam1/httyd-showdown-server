package com.mealam.showdown.lobby;

import com.mealam.showdown.lobby.data.Lobby;
import com.mealam.showdown.utils.logging.LogLevel;
import com.mealam.showdown.utils.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LobbyService {

    private final Map<String, Lobby> lobbies = new ConcurrentHashMap<>();

    public Lobby createLobby(String pHost, int pMaxPlayers) {
        String id = UUID.randomUUID().toString();
        Lobby lobby = Lobby.create(id, pHost, pMaxPlayers);
        lobbies.put(id, lobby);
        return lobby;
    }

    public Lobby joinLobby(String pLobbyId, String pPlayer) {
        Lobby lobby = lobbies.get(pLobbyId);
        if (lobby != null) {
            Lobby updatedLobby = lobby.addPlayer(pPlayer);
            lobbies.put(pLobbyId, updatedLobby);
            return updatedLobby;
        }
        Logger.log(LogLevel.ERROR, String.format("Lobby with ID %s not found", pLobbyId));
        return null;
    }

    public void deleteLobby(String pLobbyId) {
        lobbies.remove(pLobbyId);
    }

    public List<Lobby> getLobbies() {
        return new ArrayList<>(lobbies.values());
    }
}
