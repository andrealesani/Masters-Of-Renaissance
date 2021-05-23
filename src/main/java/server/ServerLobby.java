package server;

import Exceptions.network.GameFullException;
import Exceptions.network.UnknownPlayerNumberException;
import Exceptions.network.UsernameAlreadyExistsException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the game lobby.
 * It handles the creation of controllers for the games hosted on the server, and the logging in of users.
 */
public class ServerLobby {
    /**
     * List used to store the controllers of the games currently running on the server
     */
    private final List<GameController> currentGames = new ArrayList<>();

    /**
     * Checks that the given username doesn't already exist in one of the stored games.
     * Then either adds the user to the game currently in creation phase, or creates a new one all together.
     *
     * @param username the player's username
     * @param userOut  the player's writer stream
     * @return the controller for the game to which the player will be added
     * @throws UnknownPlayerNumberException   if the number of players for the game currently in creation phase hasn't yet been specified
     * @throws UsernameAlreadyExistsException if the the given username is already taken by another player
     */
    public synchronized GameController login(String username, PrintWriter userOut) throws UnknownPlayerNumberException, UsernameAlreadyExistsException {

        //Checks if the given username is already taken, and attempts to add the player to the first game that isn't full or to the one they belonged before disconnection
        for (GameController game : currentGames) {
            try {
                game.addPlayer(username, userOut);
                return game;
            } catch(GameFullException ignored) {}
        }

        GameController newGame = new GameController(username, userOut);
        currentGames.add(newGame);

        return newGame;
    }

    /**
     * Abort the game for which the player number has already been set, as its player has disconnected
     */
    public synchronized void abortGame() {
        for (GameController game : currentGames) {
            if (!game.isSizeSet()) {
                currentGames.remove(game);
                break;
            }
        }
    }
}
