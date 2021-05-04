package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientHandler implements Runnable {

    private final Socket socket;
    private final Lobby lobby;

    public ServerClientHandler(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
    }

    public void run() {

        //Creating input stream
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        //Creating output stream
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        //Validates player's login and (if necessary) asks for number of players in current game
        GameController gameController = null;

        while (gameController == null) {
            String message = in.nextLine();
            gameController = lobby.loginHandler(out, message);
            }


        //Reads and writes on the connection until it receives terminator string
        while (true) {
            String command = in.nextLine();
            if (command.equals("ESC + :q")) {
                break;
            } else {
                gameController.readCommand(out, command);

                System.out.println("End of message");
                out.println("End of message");
            }
        }

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
}
