package server;

import com.google.gson.Gson;
import network.MessageType;
import network.MessageWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class handles the server's connection with a single client
 */
public class ServerPlayerHandler implements Runnable {
    /**
     * The socket for the connection with this handler's client
     */
    private final Socket socket;
    /**
     * The server's lobby
     */
    private final ServerLobby lobby;
    /**
     * This handler's input reader
     */
    private Scanner in;
    /**
     * This handler's output writer
     */
    private PrintWriter out;
    /**
     * The controller for the game this handler's client has joined
     */
    private GameController controller;
    /**
     * The username for this player's client
     */
    private String username;
    /**
     * The handler's gson object used to serialize messages for the client
     */
    private final Gson gson;

    //CONSTRUCTORS
    //TODO wrap login messages

    /**
     * The class constructor
     *
     * @param socket the socket associated with the handler's client
     * @param lobby  the server's lobby
     */
    public ServerPlayerHandler(Socket socket, ServerLobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
        this.controller = null;
        this.gson = new Gson();
        this.username = null;
    }

    //MULTITHREADING METHODS

    /**
     * The run method for this handler.
     * It takes the client through the login phase, and then waits for them to send commands to play the game
     */
    public void run() {

        //Creating input and output streams
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }


        try {
            System.out.println("Logging in player...");

            sendMessage(MessageType.INFO, "Please, set your username.");

            //Reads the client's message
            String messageString;
            while (true) {

                messageString = in.nextLine();

                MessageWrapper message = gson.fromJson(messageString, MessageWrapper.class);

                switch (message.getType()) {
                    case LOGIN -> loginPlayer(message.getMessage());
                    case NUM_OF_PLAYERS -> setGameSize(message.getMessage());
                    case COMMAND -> runCommand(message.getMessage());
                    case PING -> System.out.println("pong");
                    default -> {
                        System.out.println("Client sent an unexpected message: ");
                        System.out.println(message.getMessage());
                        sendMessage(MessageType.ERROR, "This type of message is not supported.");
                    }
                }
            }

        } catch (NoSuchElementException ex) {
            if (controller == null) {
                System.out.println("The connection with a player in login phase was lost.");
            } else if (!controller.isSizeSet()) {
                System.out.println("The connection with player " + username + " was lost during game size setting phase.");
                lobby.abortGame();
            } else {
                System.out.println("The connection with player " + username + " was lost during the game.");
                controller.setDisconnectedStatus(username);
            }
        }

        System.out.println("Closing the connection...");

        //Close streams and socket
        in.close();
        out.close();
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        System.out.println("Connection closed.");
    }

    //PRIVATE METHODS

    /**
     * Takes the player's username and tries to add them to a game through the lobby.
     * If successful, the username is saved on the handler and the lobby return's the client's game's controller
     *
     * @param messageContent the message contained in the message wrapper
     */
    private void loginPlayer(String messageContent) throws NoSuchElementException {

        //If the player has not already logged in
        if (controller == null) {
            //Ask the lobby to validate username
            try {

                controller = lobby.login(messageContent, out);
                this.username = messageContent;

                sendMessage(MessageType.SET_USERNAME, username);

                if (!controller.isSizeSet())
                    sendMessage(MessageType.INFO, "Please, choose the game's number of players.");

            } catch (Exception ex) {
                sendMessage(MessageType.ERROR, ex.getMessage());
            }

        } else {
            System.out.println("Player attempted to login after already choosing a username.");
            sendMessage(MessageType.ERROR, "You have already chosen a username.");
        }
    }

    /**
     * If the client is the first player for a game, sets its size
     *
     * @param messageContent the message contained in the message wrapper
     */
    private void setGameSize(String messageContent) throws NoSuchElementException {

        //If controller number of players has not been decided
        if (!controller.isSizeSet()) {

            //Tries to set controller's number of players
            try {

                int size = Integer.parseInt(messageContent);
                controller.choosePlayerNumber(size);

            } catch (NumberFormatException ex) {
                sendMessage(MessageType.ERROR, "Game's number of players must be an integer.");
            } catch (Exception ex) {
                sendMessage(MessageType.ERROR, ex.getMessage());
            }
        } else {
            System.out.println("Player attempted to choose game's number of players without needing to.");
            sendMessage(MessageType.ERROR, "The game's number of players has already been decided.");
        }
    }

    /**
     * Forwards the player's command to the controller for execution
     *
     * @param messageContent the message contained in the message wrapper
     */
    private void runCommand(String messageContent) throws NoSuchElementException {

        //If player has logged in and their game's number of players has been decided
        if (controller != null && controller.isSizeSet()) {

                //Forward player command to controller
                System.out.println("Received command: " + messageContent);
                controller.readCommand(username, messageContent);
        }
    }

    /**
     * Sends a message encapsulated in a MessageWrapper to the handler's client in json form
     *
     * @param type    the type of the message
     * @param message the content of the message
     */
    private void sendMessage(MessageType type, String message) {
        out.println(
                gson.toJson(
                        new MessageWrapper(type, message)));
    }

}
