package network;

import com.google.gson.Gson;
import Exceptions.ParametersNotValidException;
import Exceptions.network.GameFullException;
import Exceptions.network.PlayerNumberAlreadySetException;
import Exceptions.network.UnknownPlayerNumberException;
import model.Game;
import model.UserCommandsInterface;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a game's controller
 */
public class GameController {
    /**
     * A gson object used to deserialize incoming commands
     */
    private final Gson gson;
    /**
     * This controller's game class
     */
    private Game game;
    /**
     * A data structure used to store the game's players and the streams used to send them messages
     */
    private Map<String, PrintWriter> players;
    /**
     * The game's number of players
     */
    int size;

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param username the first player's username
     * @param userOut  the first player's writer stream
     */
    public GameController(String username, PrintWriter userOut) {
        this.gson = new Gson();
        this.size = 0;

        this.players = new HashMap<>();
        players.put(username, userOut);
    }

    //PUBLIC METHODS

    //TODO fix responses

    /**
     * Deserializes from json and runs a single command from one of the game's players
     *
     * @param username      the username of the player that sent the message
     * @param commandString the message sent by the player, before deserialization
     */
    public void readCommand(String username, String commandString) {
        if (game == null) {
            players.get(username).println("Yo my man take a chill pill, everyone ain't here just yet.");
            System.out.println("Player tried sending a command before the game start.");
        } else if (!username.equals(getCurrentPlayerUsername())) {
            players.get(username).println("Ur not the right dude, my dude.");
            System.out.println("Wrong player tried to send a command.");
        } else {
            try {
                Command command = gson.fromJson(commandString, Command.class);
                String result = command.runCommand(game);
                if (result != null) {
                    players.get(username).println(result);
                    System.out.println("Error: " + result);
                }
            } catch (Exception ex) {
                players.get(username).println("Dude c'mon... that look like a json to you??");
                System.out.println("Player sent a message that was not a json.");
            }
        }
    }

    /**
     * Adds a player to the controller's game
     *
     * @param username the player's username
     * @param userOut  the player's writer stream
     * @throws GameFullException            if the game has already reached its full size
     * @throws UnknownPlayerNumberException if the first player has yet to decide the game's number of players
     */
    public void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException {
        if (size == 0)
            throw new UnknownPlayerNumberException();
        if (players.size() >= size)
            throw new GameFullException();

        players.put(username, userOut);
        System.out.println("Added player: " + username + " to current game.");
        checkGameStart();
    }

    /**
     * Chooses the controller's game's number of players
     *
     * @param number the number of players
     * @throws PlayerNumberAlreadySetException if the player number has already been set
     */
    public void choosePlayerNumber(int number) throws PlayerNumberAlreadySetException {
        if (size > 0) {
            throw new PlayerNumberAlreadySetException();
        }
        //TODO non hardcodare il numero massimo di giocatori?
        if (number < 1 || number > 4) {
            throw new ParametersNotValidException();
        }
        size = number;
        checkGameStart();
    }

    /**
     * Broadcasts a message to all of the controller's game's players
     *
     * @param message the message to be broadcast
     */
    public void broadcastMessage(String message) {
        for (PrintWriter out : players.values()) {
            out.println(message);
        }
    }

    /**
     * Returns whether the controller's game has reached its full size
     *
     * @return true if the current number of players is equal to the game's size
     */
    public boolean isFull() {
        if (size == 0 || players.size() < size) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether or not the game's size has already been set
     *
     * @return true if the game's size has been set
     */
    public boolean isSizeSet() {
        if (size == 0) {
            return false;
        }
        return true;
    }

    //PRIVATE METHODS

    /**
     * Checks if all of the players have joined the game.
     * If so, creates the game class and alerts the clients
     */
    private void checkGameStart() {
        if (players.size() != size)
            return;

        game = new Game(players.keySet());

        broadcastMessage("YOOOOOOO LESSS GOOOOOOOO, the game is ONNNNNNNNN!");
        broadcastMessage("First up is my dude! My man! None other than motherf*****g " + getCurrentPlayerUsername() + "!!!");
        System.out.println("The game will now start.");
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the username of the game's current player
     */
    public String getCurrentPlayerUsername() {
        if (game == null) {
            return null;
        }
        return game.getCurrentPlayer().getUsername();
    }

    /**
     * Getter
     *
     * @return a set of the game's player's usernames
     */
    public Set<String> getPlayersUsernames() {
        return players.keySet();
    }
}