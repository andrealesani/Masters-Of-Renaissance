package network;

import model.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
    private final GameLobby lobby;
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

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param socket the socket associated with the handler's client
     * @param lobby  the server's lobby
     */
    public ServerPlayerHandler(Socket socket, GameLobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
        this.controller = null;
    }

    //MULTITHREADING METHODS

    //TODO messaggi all'user fatti bene

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

        //Read the player's commands
        commandLoop();

        System.out.println("Closing the connection.");

        //Close streams and socket
        in.close();
        out.close();
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    //PRIVATE METHODS

    /**
     * Reads a username for the player, and tries to add it to a game through the lobby.
     * If successful, the username is saved on the handler and the lobby return's the client's game's controller
     */
    private void loginPlayer() {
        while (controller == null) {
            out.println("Please, set your username: ");
            String username = in.nextLine();
            try {
                controller = lobby.login(username, out);
                this.username = username;
                out.println("Username was correctly set to: " + username + ".");
            } catch (Exception ex) {
                out.println("Error: " + ex.getMessage());
            }
        }
    }

    /**
     * If the client is the first player for a game, asks them to set the game's size
     */
    private void setGameSize() {
        while (!controller.isSizeSet()) {
            out.println("Please, choose the game's number of players: ");
            String sizeString = in.nextLine();
            try {
                int size = Integer.parseInt(sizeString);
                controller.choosePlayerNumber(size);
                out.println("Game size was correctly set to: " + size + " players.");
            } catch (NumberFormatException ex) {
                out.println("Hey yo this ain't a number, dude!");
            } catch (Exception ex) {
                out.println("Error: " + ex.getMessage());
            }
        }
    }

    /**
     * Reads commands from the client, until it receives the termination string
     */
    private void commandLoop() {
        while (true) {
            String command = in.nextLine();
            if (command.equals("ESC + :q")) {
                break;
            } else {
                System.out.println("Received command: " + command);
                controller.readCommand(username, command);
            }
        }
    }

}
