package network;

import com.google.gson.Gson;
import model.Game;
import model.PlayerBoard;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the game's lobby
 */
public class Lobby {
    private final List<String> playerLobby = new ArrayList<>();
    private final List<Game> activeGames = new ArrayList<>();

    private int nextGamePlayers;
    private GameController nextGameController;
    private final Gson gson = new Gson();

    public GameController loginHandler (PrintWriter out, String message) {
        Map jsonMap;
        Map<String, String> opResult = new HashMap<>();

        //Extract json into jsonMap
        try {
            jsonMap = gson.fromJson(message, Map.class);
        } catch (Exception ex) {
            opResult.put("Result", "Error");
            opResult.put("Message", "The message is not a json file.");
            out.println(gson.toJson(opResult));
            return null;
        }

        //Extract command
        String command = (String) jsonMap.get("command");
        if (command.equals("username")) {
            String username = (String) jsonMap.get("username");
            if (nextGamePlayers > 0) {
                nextGameController = new GameController();

            }
        }

        out.println(gson.toJson(opResult));
        return nextGameController;
    };

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
