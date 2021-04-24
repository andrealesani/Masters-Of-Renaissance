package network;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private int port;

    public ServerMain(int port) {
        this.port = port;
    }

    public void startServer() {
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

        System.out.println("Server ready");

        //Creates connections with clients in new threads
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Creating new connection...");
                executor.submit(new ServerClientHandler(socket));
            } catch(IOException ex) {
                System.err.println(ex.getMessage());
                break; //If serverSocket is shut down
            }
        }

        executor.shutdown();
    }

    public static void main(String[] args) {

        //Initialize portNumber
        int portNumber = 0;
        if (args.length > 0){
            portNumber = Integer.parseInt(args[0]);
        }else{
            System.out.println("No parameters on command line: reading from Json.");
            Gson gson = new Gson();
            JsonReader reader = null;

            try {

                reader = new JsonReader(new FileReader("./src/main/java/network/HostAndPort.json"));
                Map<String, String> map = gson.fromJson(reader, Map.class);
                portNumber = Integer.parseInt(map.get("PortNumber"));

            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }

        ServerMain server = new ServerMain(portNumber); //not sure why we do this instead of running the code in main
        server.startServer();
    }
}
