package it.polimi.ingsw.server;

import it.polimi.ingsw.Exceptions.GameDataNotFoundException;
import it.polimi.ingsw.Exceptions.network.UsernameAlreadyExistsException;
import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.Exceptions.network.GameFullException;
import it.polimi.ingsw.Exceptions.network.PlayerNumberAlreadySetException;
import it.polimi.ingsw.Exceptions.network.UnknownPlayerNumberException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.StaticMethods;
import it.polimi.ingsw.network.MessageWrapper;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
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
     * This controller's game's persistence handler
     */
    private PersistenceHandler persistenceHandler;
    /**
     * Map used to associate the game's players and the streams used to send messages to each of them
     */
    private final Map<String, PrintWriter> players;
    /**
     * The game's number of players
     */
    int size;
    /**
     * Determines if the game is over or not
     */
    boolean isGameOver;

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param username the first player's username
     * @param userOut  the first player's writer stream
     */
    public GameController(String username, PrintWriter userOut) {
        this.gson = new Gson();
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        players.put(username, userOut);
        playerMessage(username, MessageType.CONFIRM_USERNAME, username);
    }

    /**
     * The class constructor for restoring a saved game
     *
     * @param persistenceHandler the PersistenceHandler for the saved game
     */
    public GameController(PersistenceHandler persistenceHandler) {
        this.gson = new Gson();
        this.players = new HashMap<>();
        this.isGameOver = false;

        this.game = persistenceHandler.restoreGame();
        game.createBeans(this);
        this.persistenceHandler = persistenceHandler;

        List<String> usernames = game.getPlayersUsernamesTurnOrder();
        this.size = usernames.size();
        for (String username : usernames)
            players.put(username, null);

        if (game.getWinner() != null)
            this.isGameOver = true;
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

        } else if (isGameOver) {

            playerMessage(username, MessageType.ERROR, "The game has already ended");
            System.out.println("Player tried to send a command after the end of the game.");

        } else {
            try {

                Command command = gson.fromJson(commandString, Command.class);

                String result = command.runCommand(game);

                if (result != null) {
                    playerMessage(username, MessageType.ERROR, result);
                    System.out.println("Error: " + result);
                } else {
                    try {
                        if (!isGameOver)
                            persistenceHandler.saveGame(game);
                    } catch (Exception ex) {
                        System.err.println("There was an error in saving the current game.");
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {

                playerMessage(username, MessageType.ERROR, "The message is not in json format.");
                System.out.println("Player sent a message that was not a json.");
            }
        }
    }

    //PUBLIC PLAYER MANAGEMENT METHODS

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
            if (players.size() >= size || isGameOver) {
                throw new GameFullException();
            }
        } else {
            if (game != null) {
                if (game.isPlayerConnected(username)) {
                    throw new UsernameAlreadyExistsException();
                } else {
                    //If the player is reconnecting
                    setConnectedStatus(username);
                    players.put(username, userOut);
                    playerMessage(username, MessageType.CONFIRM_USERNAME, username);
                    playerMessage(username, MessageType.GAME_START, "You have been reconnected to the game.");
                    System.out.println("Added player: " + username + " to current game.");
                    game.updateReconnectedPlayer(username);

                    //If the game has already ended
                    if (isGameOver)
                        playerMessage(username, MessageType.GAME_END, "The game has ended.");

                    return;
                }
            } else {
                throw new UsernameAlreadyExistsException();
            }
        }

        //If the player is connecting for the first time
        broadcastMessage(MessageType.PLAYER_CONNECTED, username);
        players.put(username, userOut);
        playerMessage(username, MessageType.CONFIRM_USERNAME, username);
        System.out.println("Added player: " + username + " to current game.");

        checkGameStart();
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
                broadcastMessage(MessageType.PLAYER_CONNECTED, username);
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
            System.out.println("Player " + username + " left and will now be removed from the game's players.");
            players.remove(username);
        } else {
            try {
                game.setDisconnectedStatus(username);
                broadcastMessage(MessageType.PLAYER_DISCONNECTED, username);
                players.put(username, null);
                System.out.println("Player " + username + " is now disconnected.");

                //If the game is over, remove the player from the game
                if (isGameOver)
                    players.remove(username);

            } catch (ParametersNotValidException ex) {
                System.err.println("Players in GameController do not correspond with games in GameModel.");
            }
        }
    }

    //PUBLIC MESSAGING METHODS

    /**
     * Sends a message encapsulated in a MessageWrapper in json form to the player with the given username
     *
     * @param username the player's username
     * @param type     the type of the message
     * @param message  the content of the message
     */
    public void playerMessage(String username, MessageType type, String message) {
        if (players.get(username) != null)
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

    //SETTERS

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

        if (number < 1 || number > 4) {
            throw new ParametersNotValidException();
        }
        size = number;
        checkGameStart();
    }

    /**
     * Warns the controller that its game has ended and warns all its players
     */
    public void setGameOver() {
        if (!isGameOver) {
            isGameOver = true;
            broadcastMessage(MessageType.GAME_END, "The game has ended.");
            try {
                StaticMethods.deleteGameData(persistenceHandler.getId());
            } catch (GameDataNotFoundException ex) {
                System.err.println("A game finished but its save file was not able to be deleted.");
                ex.printStackTrace();
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

        persistenceHandler = new PersistenceHandler();
        persistenceHandler.saveGame(game);

        System.out.println("The game will now start.");
    }

    //GETTERS

    /**
     * Returns whether or not the game's size has already been set
     *
     * @return true if the game's size has been set
     */
    public boolean isSizeSet() {
        return size != 0;
    }

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
            playersSet.removeIf(player -> !game.isPlayerConnected(player));
        }

        return playersSet;
    }

    /**
     * Getter
     *
     * @return whether the controller's game has ended or not
     */
    public Boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Returns the number of players registered to the game
     *
     * @return the number of players
     */
    public int getNumOfPlayers() {
        return players.keySet().size();
    }
}