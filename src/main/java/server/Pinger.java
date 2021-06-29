package server;

import com.google.gson.Gson;
import network.MessageType;
import network.MessageWrapper;

import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;


public class Pinger extends TimerTask {
    private PrintWriter out;

    public Pinger(PrintWriter out) {
        this.out = out;
    }

    public void run() {
        Timer timer = new Timer();
        Gson gson = new Gson();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                out.println(gson.toJson(new MessageWrapper(MessageType.PING, "")));
            }
        };

        int intialDelay = 100;
        int interval = 5000;
        timer.schedule(task, intialDelay, interval);
    }

}
