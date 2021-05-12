package network;

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
     * @throws GameFullException              if the game currently in creation phase is already full (should never happen)
     * @throws UsernameAlreadyExistsException if the there given username is already taken by another player
     */
    public synchronized GameController login(String username, PrintWriter userOut) throws UnknownPlayerNumberException, GameFullException, UsernameAlreadyExistsException {

        //Checks if the given username is already taken, and attempts to add the player to the first one that isn't full
        for (GameController game : currentGames) {
            if (game.getPlayersUsernames().contains(username)) {
                throw new UsernameAlreadyExistsException();
            }
            if (!game.isFull()) {
                game.addPlayer(username, userOut);
                return game;
            }
        }

        GameController newGame = new GameController(username, userOut);
        currentGames.add(newGame);

        return newGame;
    }
}
