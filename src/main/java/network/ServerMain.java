package network;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    //MAIN

    public static void main(String[] args) {

        //Initialize portNumber
        int portNumber = 0;
        if (args.length > 0){
            portNumber = Integer.parseInt(args[0]);
        }else{
            System.out.println("No parameters found on command line: reading them from Json.");
            Gson gson = new Gson();
            JsonReader reader = null;

            try {

                reader = new JsonReader(new FileReader("./src/main/java/network/HostAndPort.json"));
                Map map = gson.fromJson(reader, Map.class);
                portNumber = ((Double)map.get("portNumber")).intValue();

            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }

        startServer(portNumber);
    }

    //PRIVATE METHODS

    private static void startServer(int port) {
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Server started!");

        //Creates connection socket
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        System.out.println("Server ready for connections!");

        //Creates connections with clients
        Lobby lobby = new Lobby();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Creating new connection...");
                executor.submit(new ServerClientHandler(socket, lobby));
            } catch(IOException ex) {
                System.err.println(ex.getMessage());
                break;
            }
        }

        executor.shutdown();
    }
}
