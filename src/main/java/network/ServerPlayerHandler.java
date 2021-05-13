package network;

import com.google.gson.Gson;
import network.beans.MessageType;
import network.beans.MessageWrapper;

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

        System.out.println("Logging in player...");

        //Login the player with a username
        loginPlayer();

        //Let the player choose the game size, if necessary
        setGameSize();

        System.out.println("Listening for player commands...");

        //Read the player's messages
        messageLoop();

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
     * Reads a username for the player, and tries to add it to a game through the lobby.
     * If successful, the username is saved on the handler and the lobby return's the client's game's controller
     */
    private void loginPlayer() {
        while (controller == null) {

            sendMessage(MessageType.INFO, "Please, set your username.");

            String username = in.nextLine();

            try {

                controller = lobby.login(username, out);
                this.username = username;
                sendMessage(MessageType.INFO, "Username was correctly set to: " + username + ".");

            } catch (Exception ex) {

                sendMessage(MessageType.ERROR, ex.getMessage());

            }
        }
    }

    /**
     * If the client is the first player for a game, asks them to set the game's size
     */
    private void setGameSize() {
        while (!controller.isSizeSet()) {

            sendMessage(MessageType.INFO, "Please, choose the game's number of players.");

            String sizeString = in.nextLine();

            try {

                int size = Integer.parseInt(sizeString);
                controller.choosePlayerNumber(size);
                sendMessage(MessageType.INFO, "Game size was correctly set to: " + size + " players.");

            } catch (NumberFormatException ex) {

                sendMessage(MessageType.ERROR, "Game's number of players must be an integer.");

            } catch (Exception ex) {

                sendMessage(MessageType.ERROR, ex.getMessage());

            }
        }
    }

    /**
     * Reads messages from the client, until it receives the termination string
     */
    private void messageLoop() {
        String message;
        while (true) {

            try {
                message = in.nextLine();
            } catch (IllegalStateException ex) {
                System.out.println("Illegal state.");
                break;
            } catch (NoSuchElementException ex) {
                System.out.println("The connection with player " + username + " was closed.");
                break;
            }

            if (message.equals("ESC + :q")) {

                break;

            } else {

                System.out.println("Received message: " + message);
                controller.readCommand(username, message);

            }
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
