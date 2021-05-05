package network;

import model.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerPlayerHandler implements Runnable {

    private final Socket socket;
    private final GameLobby lobby;
    private Scanner in;
    private PrintWriter out;
    private GameController controller;
    private String username;

    //CONSTRUCTORS

    public ServerPlayerHandler(Socket socket, GameLobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
        this.controller = null;
    }

    //MULTITHREADING METHODS

    //TODO messaggi all'user fatti bene
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

        loginPlayer();

        setGameSize();

        System.out.println("Listening for player commands...");

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

    private void loginPlayer() {
        out.println("Please, set your username: ");
        while (controller == null) {
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

    private void setGameSize() {
        out.println("Please, choose the game's number of players: ");
        while (!controller.isSizeSet()) {
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
