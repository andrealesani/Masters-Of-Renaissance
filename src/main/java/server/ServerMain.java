package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the executable for the game on the server machine
 */
public class ServerMain implements Runnable{

    //MAIN

    /**
     * The server's 'main'
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Initialize portNumber
        int portNumber = 0;
        if (args != null && args.length > 0) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            System.out.println("No parameters found on command line: reading them from Json.");
            Gson gson = new Gson();

            Reader reader = new InputStreamReader(ServerMain.class.getResourceAsStream("/json/HostAndPort.json"), StandardCharsets.UTF_8);
            Map map = gson.fromJson(reader, Map.class);
            portNumber = ((Double) map.get("portNumber")).intValue();

        }

        //Start the server
        startServer(portNumber);
    }

    //PRIVATE METHODS

    /**
     * Reads connections from the given port and creates new threads to handle them
     *
     * @param port the port from which to take connections
     */
    private static void startServer(int port) {
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Server started!");

        if (port == -1) {
            while (true) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/clientToServer"));
                    PrintWriter out = new PrintWriter(new FileWriter("/serverToClient"), true);
                    in.readLine();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    break;
                }
            }
        } else {
            //Creates connection socket
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return;
            }

            System.out.println("Server ready for connections!");

            //Creates the lobby for this server
            ServerLobby lobby = new ServerLobby();

            //Creates connections with clients on new threads
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Creating new connection...");
                    executor.submit(new ServerPlayerHandler(socket, lobby));
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    break;
                }
            }

            executor.shutdown();
        }

    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        startServer(-1);
    }
}
