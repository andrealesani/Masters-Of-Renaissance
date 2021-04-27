package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientHandler implements Runnable{

    private final Socket socket;
    public ServerClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        //Create Controller for this player
        Controller controller = new Controller();

        //Reads and writes on the connection until it receives terminator string
        while (true) {
            String command = in.nextLine();
            if (command.equals("ESC + :q")) {
                break;
            } else {
                controller.readCommand(out, command);
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
            return;
        }
    }
}
