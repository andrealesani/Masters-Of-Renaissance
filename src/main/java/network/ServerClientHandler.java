package network;

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

        //Reads and writes on the connection until it receives terminator string
        while (true) {
            String line = in.nextLine();
            if (line.equals("ESC + :q")) {
                break;
            } else {
                System.out.println("Received: " + line);
                out.println("Received: " + line);
            }
        }

        System.out.println("Closing a connection.");

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
