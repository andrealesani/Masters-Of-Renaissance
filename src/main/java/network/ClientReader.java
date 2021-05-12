package network;

import com.google.gson.Gson;
import network.beans.MarketBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class ClientReader implements Runnable {

    private final BufferedReader in;
    private boolean doStop = false;

    //CONSTRUCTORS

    public ClientReader(BufferedReader in) {
        this.in = in;
    }

    //MULTITHREADING METHODS

    public void run() {
        Gson gson = new Gson();
        while (!doStop) {
            try {
                String response = in.readLine();
                Map responseMap = gson.fromJson(response, Map.class);
                if(responseMap.get("type").equals("INFO") || responseMap.get("type").equals("ERROR"))
                    System.out.println(responseMap.get("jsonMessage"));
                else
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
