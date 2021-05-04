package network;

import com.google.gson.Gson;
import model.Game;
import model.UserCommandsInterface;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    final Gson gson;
    UserCommandsInterface game = null;

    //CONSTRUCTORS

    public GameController() {
        this.gson = new Gson();
        this.game = new Game();
    }

    //PUBLIC METHODS

    public void readCommand(PrintWriter serverOut, String message) {

        System.out.println("Received: " + message);

        Map jsonMap;
        Map<String, String> opResult = new HashMap<>();

        //Extract json into jsonMap
        try {
            jsonMap = gson.fromJson(message, Map.class);
        } catch (Exception ex) {
            opResult.put("Result", "Error");
            opResult.put("Message", "The message is not a json file.");
            serverOut.println(gson.toJson(opResult));
        }
    }
}