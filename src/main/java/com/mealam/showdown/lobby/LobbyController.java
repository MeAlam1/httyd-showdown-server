package com.mealam.showdown.lobby;

import com.mealam.showdown.lobby.data.Lobby;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService pLobbyService) {
        this.lobbyService = pLobbyService;
    }

    @GetMapping("/lobbies")
    public List<Lobby> getLobbies() {
        return lobbyService.getLobbies();
    }

    //@PostMapping("/create")
    @GetMapping("/create")
    public Lobby createLobby(@RequestParam String host, @RequestParam int maxPlayers) {
        return lobbyService.createLobby(host, maxPlayers);
    }

    @GetMapping("/join")
    public Lobby joinLobby(@RequestParam String lobbyId, @RequestParam String player) {
        return lobbyService.joinLobby(lobbyId, player);
    }
}
