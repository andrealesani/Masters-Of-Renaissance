package network;

import com.google.gson.Gson;
import model.CardColor;
import model.ResourceType;
import model.resource.Resource;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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

        //Call method the name of which is in 'command'
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

    private void chooseBonusResourceType (PrintWriter serverOut, Map map) {
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));

        System.out.println("Choosing bonus resource: " + resource.getType() + ", in quantity: " + quantity);
        serverOut.println("Choosing bonus resource: " + resource.getType() + ", in quantity: " + quantity);
    }

    private void chooseLeaderCard (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));

        System.out.println("Choosing leader card number: " + number);
        serverOut.println("Choosing leader card number: " + number);
    }

    private void playLeaderCard (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));

        System.out.println("Playing leader card number: " + number);
        serverOut.println("Playing leader card number: " + number);
    }

    private void discardLeaderCard (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));

        System.out.println("Discarding leader card number: " + number);
        serverOut.println("Discarding leader card number: " + number);
    }

    private void selectMarketRow (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));

        System.out.println("Selecting market row number: " + number);
        serverOut.println("Selecting market row number: " + number);
    }

    private void selectMarketColumn (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));

        System.out.println("Selecting market column number: " + number);
        serverOut.println("Selecting market column number: " + number);
    }

    private void sendResourceToDepot (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));
        Resource resource =  extractResource(map.get("resource"));
        int quantity =  extractInt(map.get("quantity"));

        System.out.println("Sending: " + quantity + " resources of type: " + resource.getType() + " to depot number: " + number);
        serverOut.println("Sending: " + quantity + " resources of type: " + resource.getType() + " to depot number: " + number);
    }

    private void chooseMarbleConversion (PrintWriter serverOut, Map map) {
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));

        System.out.println("Converting white marbles to resource: " + resource.getType() + ", in quantity: " + quantity);
        serverOut.println("Converting white marbles to resource: " + resource.getType() + ", in quantity: " + quantity);
    }

    private void swapDepotContent (PrintWriter serverOut, Map map) {
        int[] depots = extractIntArray(map.get("depots"));

        System.out.println("Swapping depot: " + depots[0] + " with depot: " + depots[1]);
        serverOut.println("Swapping depot: " + depots[0] + " with depot: " + depots[1]);
    }

    private void moveDepotContent (PrintWriter serverOut, Map map) {
        int[] depots = extractIntArray(map.get("depots"));
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));

        System.out.println("Moving: " + quantity + " of resource: " + resource.getType() + " from depot: " + depots[0] + " to depot: " + depots[1]);
        serverOut.println("Moving: " + quantity + " of resource: " + resource.getType() + " from depot: " + depots[0] + " to depot: " + depots[1]);
    }

    private void takeDevelopmentCard (PrintWriter serverOut, Map map) {
        CardColor color = extractColor(map.get("color"));
        int level = extractInt(map.get("level"));
        int number =  extractInt(map.get("number"));

        System.out.println("Buying development card of color: " + color + " and level: " + level + ", and placing it in slot: " + number);
        serverOut.println("Buying development card of color: " + color + " and level: " + level + ", and placing it in slot: " + number);
    }

    private void selectProduction (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));

        System.out.println("Selecting production number: " + number);
        serverOut.println("Selecting production number: " + number);
    }

    private void resetProductionChoice (PrintWriter serverOut, Map map) {
        System.out.println("Resetting chosen productions");
        serverOut.println("Resetting chosen productions");
    }

    private void confirmProductionChoice (PrintWriter serverOut, Map map) {
        System.out.println("Confirming chosen productions");
        serverOut.println("Confirming chosen productions");
    }

    private void chooseJollyInput (PrintWriter serverOut, Map map) {
        Resource resource = extractResource(map.get("resource"));

        System.out.println("Converting one jolly resource in production input to a: " + resource.getType());
        serverOut.println("Converting one jolly resource in production input to a: " + resource.getType());
    }

    private void chooseJollyOutput (PrintWriter serverOut, Map map) {
        Resource resource = extractResource(map.get("resource"));

        System.out.println("Converting one jolly resource in production output to a: " + resource.getType());
        serverOut.println("Converting one jolly resource in production output to a: " + resource.getType());
    }

    private void payFromWarehouse (PrintWriter serverOut, Map map) {
        int number =  extractInt(map.get("number"));
        Resource resource =  extractResource(map.get("resource"));
        int quantity =  extractInt(map.get("quantity"));

        System.out.println("Taking: " + quantity + " resources of type: " + resource.getType() + " from depot number: " + number);
        serverOut.println("Taking: " + quantity + " resources of type: " + resource.getType() + " from depot number: " + number);
    }

    private void payFromStrongbox (PrintWriter serverOut, Map map) {
        Resource resource =  extractResource(map.get("resource"));
        int quantity =  extractInt(map.get("quantity"));

        System.out.println("Taking: " + quantity + " resources of type: " + resource.getType() + " from strongbox");
        serverOut.println("Taking: " + quantity + " resources of type: " + resource.getType() + " from strongbox");
    }

    private void endTurn (PrintWriter serverOut, Map map) {
        System.out.println("Ending current turn");
        serverOut.println("Ending current turn");
    }

    //EXTRACTORS

    private int extractInt (Object obj) {
        return ((Double)obj).intValue();
    }

    private Resource extractResource (Object obj) {
        return (ResourceType.valueOf((String) obj)).toResource();
    }

    private CardColor extractColor (Object obj) {
        return CardColor.valueOf((String) obj);
    }

    private int[] extractIntArray (Object obj) {
        return ((ArrayList<Double>)obj).stream().mapToInt(Double::intValue).toArray();
    }

}
