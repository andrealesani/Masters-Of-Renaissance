package server;

import Exceptions.network.UsernameAlreadyExistsException;
import com.google.gson.Gson;
import Exceptions.ParametersNotValidException;
import Exceptions.network.GameFullException;
import Exceptions.network.PlayerNumberAlreadySetException;
import Exceptions.network.UnknownPlayerNumberException;
import model.Game;
import network.Command;
import network.MessageType;
import network.beans.MessageWrapper;

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
     * The server lobby
     */
    //private final ServerLobby lobby;
    /**
     * This controller's game class
     */
    private Game game;
    /**
     * Map used to associate the game's players and the streams used to send messages to each of them
     */
    private final Map<String, PrintWriter> players;
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

        //this.lobby = lobby;
        this.players = new HashMap<>();
        players.put(username, userOut);
        playerMessage(username, MessageType.SET_USERNAME, username);
    }

    //PUBLIC METHODS

    /**
     * Deserializes from json and runs a single command from one of the game's players
     *
     * @param username      the username of the player that sent the message
     * @param commandString the message sent by the player, before deserialization
     */
    public synchronized void readCommand(String username, String commandString) {
        if (game == null) {

            playerMessage(username, MessageType.ERROR, "The game has not yet started, as some players are still missing.");
            System.out.println("Player tried sending a command before the game start.");

        } else if (!username.equals(getCurrentPlayerUsername())) {

            playerMessage(username, MessageType.ERROR, "It is not your turn to act.");
            System.out.println("Wrong player tried to send a command.");

        } else {
            try {

                Command command = gson.fromJson(commandString, Command.class);

                String result = command.runCommand(game);

                if (result != null) {
                    playerMessage(username, MessageType.ERROR, result);
                    System.out.println("Error: " + result);
                }

            } catch (Exception ex) {

                playerMessage(username, MessageType.ERROR, "The message is not in json format.");
                System.out.println("Player sent a message that was not a json.");

            }
        }
    }

    /**
     * Adds a player to the controller's game
     *
     * @param username the player's username
     * @param userOut  the player's writer stream
     * @throws GameFullException              if the game has already reached its full size
     * @throws UnknownPlayerNumberException   if the first player has yet to decide the game's number of players
     * @throws UsernameAlreadyExistsException if the there given username is already taken by another player who is still connected
     */
    public void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException, UsernameAlreadyExistsException {
        if (size == 0)
            throw new UnknownPlayerNumberException();

        if (!players.containsKey(username)) {
            if (players.size() >= size) {
                throw new GameFullException();
            }
        } else {
            if (game!=null) {
                if (game.isConnected(username)) {
                    throw new UsernameAlreadyExistsException();
                } else {
                    setConnectedStatus(username);
                    players.put(username, userOut);
                    playerMessage(username, MessageType.GAME_START, "You have been reconnected to the game.");
                    playerMessage(username, MessageType.SET_USERNAME, username);
                    System.out.println("Added player: " + username + " to current game.");
                    game.updateReconnectedPlayer(username);
                    return;
                }
            } else {
                throw new UsernameAlreadyExistsException();
            }
        }

        players.put(username, userOut);
        playerMessage(username, MessageType.SET_USERNAME, username);
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
     * Sends a message encapsulated in a MessageWrapper in json form to the player with the given username
     *
     * @param username the player's username
     * @param type     the type of the message
     * @param message  the content of the message
     */
    public void playerMessage(String username, MessageType type, String message) {
        players.get(username).println(
                gson.toJson(
                        new MessageWrapper(type, message)));
    }

    /**
     * Broadcasts a message encapsulated in a MessageWrapper in json form to all of the controller's game's players
     *
     * @param type    the type of the message
     * @param message the content of the message
     */
    public void broadcastMessage(MessageType type, String message) {
        for (String player : players.keySet()) {
            playerMessage(player, type, message);
        }
    }

    /**
     * Returns whether or not the game's size has already been set
     *
     * @return true if the game's size has been set
     */
    public boolean isSizeSet() {
        return size != 0;
    }

    /**
     * Communicates to the Game that the given player has reconnected
     *
     * @param username the player's username
     */
    public synchronized void setConnectedStatus(String username) {
        if (!players.containsKey(username)) {
            System.out.println("Connection status was attempted to be set for a player that does not belong to the game.");
            return;
        }

        if (game == null) {
            System.out.println("Warning: a player attempted to be reconnected before game start, which should never happen.");
        } else {
            try {
                game.setConnectedStatus(username);
                System.out.println("Player " + username + " is now connected.");
            } catch (ParametersNotValidException ex) {
                System.out.println("Players in GameController do not correspond with games in GameModel.");
            }
        }
    }

    /**
     * Communicates to the Game that the given player has disconnected
     *
     * @param username the player's username
     */
    public void setDisconnectedStatus(String username) {
        if (!players.containsKey(username)) {
            System.out.println("Connection status was attempted to be set for a player that does not belong to the game.");
            return;
        }

        if (game == null) {
            System.out.println("Player " + username + " will now be removed from the game's players.");
            players.remove(username);
            /*
            if (players.size() == 0) {
                lobby.abortGame();
            }
             */
        } else {
            try {
                game.setDisconnectedStatus(username);
                System.out.println("Player " + username + " is now disconnected.");
            } catch (ParametersNotValidException ex) {
                System.out.println("Players in GameController do not correspond with games in GameModel.");
            }
        }
    }

    //PRIVATE METHODS

    /**
     * Checks if all of the players have joined the game.
     * If so, creates the game class and alerts the clients
     */
    private void checkGameStart() {
        if (game != null) {
            return;
        }

        if (players.size() != size) {
            broadcastMessage(MessageType.WAIT_PLAYERS, "One player has joined, waiting for more players...");
            return;
        }

        broadcastMessage(MessageType.GAME_START, "The last player has joined, the game will now commence...");

        game = new Game(players.keySet());
        game.createBeans(this);
        //broadcastMessage(MessageType.INFO, "The first player in turn order is: " + getCurrentPlayerUsername() + ".");
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
     * @return a set of the game's connected player's usernames
     */
    public Set<String> getConnectedPlayersUsernames() {
        Set<String> playersSet = players.keySet();

        if (game != null) {
            playersSet.removeIf(player -> !game.isConnected(player));
        }

        return playersSet;
    }
}