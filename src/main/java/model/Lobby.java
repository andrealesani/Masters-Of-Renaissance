package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the game's lobby
 */
public class Lobby {
    private final List<String> playerLobby = new ArrayList<>();
    private int nextGamePlayers;
    private final List<Game> activeGames = new ArrayList<>();

    /**
     * Creates a Game with the number of players specified in nextGamePlayers. The logic used to choose the players
     * that enter the Game is FIFO
     */
    public void createNewGame() {
        List<String> playersEnteringGame = new ArrayList<>();
        for (int i = 0; i < nextGamePlayers; i++) {
            playersEnteringGame.add(playerLobby.get(0));
            playerLobby.remove(0);
        }
        activeGames.add(new Game(playersEnteringGame));
        nextGamePlayers = 0;
    }

    /**
     * Adds a player to the Lobby
     *
     * @param nickname is a String that specifies the nickname the player has chosen
     */
    public void addPlayer(String nickname) {
        playerLobby.add(nickname);
    }

    /**
     * Sets the number of players that the next Game will have
     *
     * @param num specifies the number of players for the next Game
     */
    public void setNextGamePlayers(int num) {
        nextGamePlayers = num;
    }

    /**
     * This method is used to check that the username chosen by a new client is not currently used by any other client
     * already connected to the server. This guarantees that every username is unique so that if a player disconnects
     * from his game he can reconnect specifying his username
     *
     * @param newUser is the nickname that needs to be checked
     * @return true if the nickname is not currently in use and can be assigned to the client
     */
    public boolean isUsernameAvailable(String newUser) {
        for (String username : playerLobby) {
            if (username.equals(newUser))
                return false;
        }
        for (Game game : activeGames)
            for (PlayerBoard player : game.getPlayersTurnOrder())
                if (player.getUsername().equals(newUser))
                    return false;

        return true;
    }
}
