package network;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ServerMain {

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
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        }

        System.out.println("Server started!");

        //Creates connection socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        System.out.println("Waiting for connection..");

        //Waits for connection from client
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        System.out.println("Connected!");

        BufferedReader in = null;
        try {
            in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        readLoop(in, out);
    }

    private static void readLoop(BufferedReader in, PrintWriter out) {
        String s;
        try {
            while ((s = in.readLine()) != null) {
                out.println(s);
                System.out.println(s);
            }
        } catch (IOException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}
