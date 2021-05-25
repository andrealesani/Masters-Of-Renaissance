package client.CLI;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CLI {
    //MAIN

    public static void main(String[] args) {

        //Initialize hostName and portNumber
        String hostName = null;
        int portNumber = 0;
        if (args != null && args.length > 0) {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        } else {
            System.out.println("No parameters on command line: reading from Json.");
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(CLI.class.getResourceAsStream("/json/HostAndPort.json"), StandardCharsets.UTF_8);
            Map map = gson.fromJson(reader, Map.class);
            hostName = (String) map.get("hostName");
            portNumber = ((Double) map.get("portNumber")).intValue();
        }

        System.out.println("Attempting connection...");

        //Attempts connection to server
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(hostName, portNumber);
        } catch (IOException ex) {
            System.err.println("Server is not online");
            System.exit(1);
        }

        System.out.println("Client connected!");

        startCLI(clientSocket);
    }

    //PRIVATE METHODS

    private static void startCLI(Socket clientSocket) {
        //ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);

            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));

            CLIView CLIView = new CLIView();

            CLIReader CLIReader = new CLIReader(in, CLIView, latch);
            Thread readerThread = new Thread(CLIReader);
            readerThread.start();
            //executor.submit(CLIReader);

            CLIWriter CLIWriter = new CLIWriter(stdIn, out, CLIView, latch);
            Thread writerThread = new Thread(CLIWriter);
            writerThread.start();
            //executor.submit(CLIWriter);

            try {
                latch.await();
            } catch (InterruptedException ex) {
                System.err.println("Latch was interrupted.");
            }

            CLIWriter.doClose();

        } catch (IOException ex) {
            System.out.println("Uh-oh, there's been an IO problem!");
        }

        //executor.shutdownNow();
        System.out.println("Shut down.");
    }
}
