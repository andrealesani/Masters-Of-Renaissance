package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {

        //String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        //Aggiungere alternativa lettura parametri da JSON

        System.out.println("Server started!");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Accepting..");

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Accepted");
        } catch (IOException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }

        BufferedReader in;
        try {
            in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
            readLoop(in);
        } catch (IOException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void readLoop(BufferedReader in) {
        String s;
        try {
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
