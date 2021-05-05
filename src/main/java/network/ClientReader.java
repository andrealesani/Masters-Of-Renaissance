package network;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader implements Runnable {

    private final BufferedReader in;
    private boolean doStop = false;

    //CONSTRUCTORS

    public ClientReader(BufferedReader in) {
        this.in = in;
    }

    //MULTITHREADING METHODS

    public void run() {
        while (!doStop) {
            try {
                String response = in.readLine();
                System.out.println("Server says: " + response);
            } catch (IOException ex) {
                System.out.println("Uh-oh, something went wrong while reading from the connection!");
            }
        }
    }

    public void doStop() {
        doStop = true;
    }
}
