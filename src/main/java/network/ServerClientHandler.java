package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientHandler implements Runnable {

    private final Socket socket;
    private final GameLobby lobby;
    private String username;

    public ServerClientHandler(Socket socket, GameLobby lobby) {
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

        System.out.println("Logging in player...");

        //logs in the player
        GameController controller = null;
        while (controller == null) {
            String username = in.nextLine();
            try {
                controller = lobby.login(username, out);
                this.username = username;
            } catch (Exception ex) {
                out.println(ex.getMessage());
                //TODO inviare eccezione in un bel pacchetto
            }
        }

        System.out.println("Listening for player commands...");

        //Forwards the commands to the game's controller class until it receives the terminator string
        while (true) {
            String command = in.nextLine();
            if (command.equals("ESC + :q")) {
                break;
            } else {
                controller.readCommand(username, command);

                //TODO eliminare
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
