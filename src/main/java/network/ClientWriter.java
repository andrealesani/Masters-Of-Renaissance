package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

public class ClientWriter implements Runnable {

    private final BufferedReader stdIn;
    private final PrintWriter out;
    private final ClientView clientView;
    private final CountDownLatch latch;

    //CONSTRUCTORS

    public ClientWriter(BufferedReader stdIn, PrintWriter out, ClientView clientView, CountDownLatch latch) {
        this.stdIn = stdIn;
        this.out = out;
        this.clientView = clientView;
        this.latch = latch;
    }

    //MULTITHREADING METHODS

    public void run() {

        String userInput;
        while (true) {

            try {
                userInput = stdIn.readLine();
            } catch (IOException ex) {
                System.out.println ("Uh oh, IO exception when reading from stdIn.");
                break;
            }

            if (userInput.equals("ESC + :q")) {
                out.println(userInput);
                System.out.println("Closing connection...");
                break;
            }

            if (userInput.contains("show")) {
                System.out.println("\n\n" + clientView);
            } else
                out.println(userInput);
        }

        latch.countDown();
    }
}
