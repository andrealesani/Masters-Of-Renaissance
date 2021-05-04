package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientMain {

    //MAIN

    public static void main(String[] args) throws IOException {

        //Initialize hostName and portNumber
        String hostName = null;
        int portNumber = 0;
        if (args.length > 0){
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        }else{
            System.out.println("No parameters on command line: reading from Json.");
            Gson gson = new Gson();
            JsonReader reader;

            try {

                reader = new JsonReader(new FileReader("./src/main/java/network/HostAndPort.json"));
                Map map = gson.fromJson(reader, Map.class);
                hostName = (String)map.get("hostName");
                portNumber = ((Double)map.get("portNumber")).intValue();

            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }

        System.out.println("Attempting connection...");

        //Attempts connection to server
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(hostName, portNumber);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        System.out.println("Client connected!");

        try {
            connectionLoop(clientSocket);
        } catch (IOException ex) {
            System.out.println("Uh oh, something went wrong in the connection loop");
        }
    }

    //PRIVATE METHODS

    private static void connectionLoop(Socket clientSocket) throws IOException {
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);

        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

        BufferedReader stdIn =
                new BufferedReader(
                        new InputStreamReader(System.in));

        //Reads input string and sends it to server, and prints the answer
        String userInput;
        String serverResponse;
        Gson gson = new Gson();
        Map<String, String> jsonMap;

        while ((userInput = stdIn.readLine()) != null) {
            try {
                out.println(userInput);
                if (userInput.equals("ESC + :q")) {
                    System.out.println("Closing connection...");
                    break;
                } else {
                    while (!(serverResponse = in.readLine()).equals("End of message")) {
                        try {
                            System.out.println("Unprocessed response: " + serverResponse);
                            jsonMap = gson.fromJson(serverResponse, Map.class);
                            System.out.println("Response: " + jsonMap.get("Result"));
                        } catch (Exception ex) {
                            System.err.println(ex.getMessage());
                        }
                    }
                    System.out.println("Server has finished answering");
                }
            } catch(IOException ex) {
                System.err.println(ex.getMessage());
                return;
            }
        }
    }
}
