package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.ClientReader;
import it.polimi.ingsw.client.ClientView;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This class represents the executable for the game's Command Line Interface on the client machine
 */
public class CLI {

    //MAIN

    /**
     * Main method of the CLI, which is called from the launcher in case user chooses CLI from the options.
     *
     * @param args of type String[] - parsed arguments.
     */
    public static void main(String[] args) {

        //Initialize hostName and portNumber
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(CLI.class.getResourceAsStream("/json/HostAndPort.json"), StandardCharsets.UTF_8);
        Map map = gson.fromJson(reader, Map.class);

        BufferedReader stdIn =
                new BufferedReader(
                        new InputStreamReader(System.in));

        //Creates the socket to connect with the server
        Socket clientSocket = createSocket(stdIn, map);

        System.out.println("Client connected!");

        //Starts the CLI
        startCLI(clientSocket, stdIn);
    }

    //PRIVATE METHODS

    /**
     * Asks the user's input to determine which host ip and port to connect to
     *
     * @param stdIn       the stream for the user's input
     * @param hostAndPort the map containing the default host and port
     * @return the socket for the connection with the server
     */
    private static Socket createSocket(BufferedReader stdIn, Map hostAndPort) {
        String defaultHostName = (String) hostAndPort.get("hostName");
        int defaultPortNumber = ((Double) hostAndPort.get("portNumber")).intValue();

        String hostName;
        int portNumber;

        while (true) {

            System.out.println("Please select which server to connect to: \n");

            //Asks the player for the host ip

            System.out.println( "Default host ip is '" + defaultHostName + "', " +
                                "press ENTER to confirm or insert a new one to change it.");
            try {

                hostName = stdIn.readLine();
                if (hostName.equals(""))
                    hostName = defaultHostName;

            } catch (IOException e) {
                System.err.println("Error when trying to read host ip, using default.");
                hostName = defaultHostName;
            }

            //Asks the player for the port

            System.out.println( "Default host port is '" + defaultPortNumber + "', " +
                                "press ENTER to confirm or insert a new one to change it.");
            try {

                String portString = stdIn.readLine();
                if (portString.equals(""))
                    portNumber = defaultPortNumber;
                else
                    portNumber = Integer.parseInt(portString);

            } catch (IOException | NumberFormatException e) {
                System.err.println("Error when trying to read host port, using default.");
                portNumber = defaultPortNumber;
            }

            //Attempts connection to server

            Socket clientSocket = null;
            try {
                System.out.println("Attempting connection to '" + hostName + ":" + portNumber + "'...");
                clientSocket = new Socket(hostName, portNumber);
                return clientSocket;
            } catch (IOException ex) {
                System.err.println("Selected server is not online.");
            } catch (IllegalArgumentException ex) {
                System.err.println("Invalid port number.");
            }
        }
    }

    /**
     * Start the Command Line Interface
     *
     * @param clientSocket the socket for the connection with the server
     * @param stdIn        the stream taken from the user's input
     */
    private static void startCLI(Socket clientSocket, BufferedReader stdIn) {
        //Creates a countdown latch to ensure that the program shuts down if either the reading or writing threads fail
        CountDownLatch latch = new CountDownLatch(1);

        try {

            //Sets a 10 second timeout for the socket reader
            try {
                clientSocket.setSoTimeout(10 * 1000);
            } catch (SocketException e) {
                System.err.println("Warning: couldn't set socket timeout in CLI");
                e.printStackTrace();
            }

            //Creates the input and output to and from the server, and the input from command line
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);

            //Creates the object which stores the game data
            ClientView ClientView = new ClientView();

            //Creates the thread that processes messages from the player
            CLIWriter CLIWriter = new CLIWriter(stdIn, out, ClientView, latch);
            Thread writerThread = new Thread(CLIWriter);
            writerThread.start();

            //Creates the thread that processes messages from the server
            ClientReader ClientReader = new ClientReader(in, CLIWriter, ClientView, latch);
            Thread readerThread = new Thread(ClientReader);
            readerThread.start();

            //Awaits termination by one of the threads
            try {
                latch.await();
            } catch (InterruptedException ex) {
                System.err.println("Latch was interrupted.");
            }

            //Kills the writer thread
            CLIWriter.doClose();

        } catch (IOException ex) {
            System.out.println("Uh-oh, there's been an IO problem!");
        }

        System.out.println("Shut down.");
    }
}
