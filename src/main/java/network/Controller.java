package network;

import com.google.gson.Gson;
import model.ResourceType;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Controller {

    //PUBLIC METHODS

    public void readCommand (PrintWriter serverOut, String message) {

        System.out.println("Received: " + message);

        Gson gson = new Gson();
        Map map;

        //Extract json into map
        try {
            map = gson.fromJson(message, Map.class);
        } catch (Exception ex) {
            serverOut.println(ex.getMessage());
            return;
        }

        //Extract command from map
        String command = (String) map.get("command");
        if (command==null) {
            serverOut.println("Error: Message does not contain a valid command.");
            return;
        } else {
            System.out.println("Command type: " + command);
            serverOut.println("Received: " + command);
        }

        //Call method whose name is in command
        java.lang.reflect.Method commandMethod = null;
        try {
            commandMethod = Controller.class.getDeclaredMethod(command, PrintWriter.class, Map.class);
        } catch (SecurityException | NoSuchMethodException ex) {
            serverOut.println(ex.getMessage());
            return;
        }
        try {
            commandMethod.invoke(this, serverOut, map);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            serverOut.println(ex.getMessage());
            return;
        }

    }

    //PRIVATE METHODS

    private void chooseLeaderCard (PrintWriter serverOut, Map map) {
        int number =  ((Double)map.get("number")).intValue();

        System.out.println("choosing leader card number: " + number);
        serverOut.println("Received number: " + number);
    }

}
