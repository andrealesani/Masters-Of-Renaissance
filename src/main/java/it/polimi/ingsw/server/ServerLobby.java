package it.polimi.ingsw.server;

import it.polimi.ingsw.Exceptions.GameDataNotFoundException;
import it.polimi.ingsw.Exceptions.network.GameFullException;
import it.polimi.ingsw.Exceptions.network.UnknownPlayerNumberException;
import it.polimi.ingsw.Exceptions.network.UsernameAlreadyExistsException;
import it.polimi.ingsw.StaticMethods;

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
    private final List<GameController> currentGames;

    //CONSTRUCTORS

    /**
     * Constructor. Takes care of restoration of saved games
     */
    public ServerLobby() {
        currentGames = new ArrayList<>();

        try {
            List<PersistenceHandler> restoredGames = StaticMethods.restoreGames();

            for (PersistenceHandler handler : restoredGames) {
                currentGames.add(new GameController(handler));
                System.out.println("Restored game with id " + handler.getId() + ".");
            }

        } catch (GameDataNotFoundException ex) {
            System.err.println("Failed to restore saved games.");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Unspecified error when trying to restore saved games.");
            ex.printStackTrace();
        }
    }

    //PUBLIC METHODS

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
        for (int i = 0; i < currentGames.size(); i++) {
            GameController game = currentGames.get(i);

            //If all players have left during creation phase, or after the game has ended, remove the game from the list
            if (game.getNumOfPlayers() == 0) {
                System.out.println("Deleted a game.");
                currentGames.remove(game);
                i--;
            } else {
                //Attempts to add the player to the game
                try {
                    game.addPlayer(username, userOut);
                    return game;
                } catch (GameFullException ignored) {}
            }
        }

        //If there were no games waiting for players, create a new one
        GameController newGame = new GameController(username, userOut);
        currentGames.add(newGame);

        return newGame;
    }

    /**
     * Remove the given controller from the current games list
     *
     * @param controller the controller to be removed
     */
    public synchronized void abortGame(GameController controller) {
        currentGames.remove(controller);
    }
}
