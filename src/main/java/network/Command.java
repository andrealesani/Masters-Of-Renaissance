package network;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.CardColor;
import model.Game;
import model.ResourceType;
import model.resource.Resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Command {
    Game game = null;
    private String command;
    private String attributes;

    public void run(Game game) {
        Gson gson = new Gson();
        this.game = game;
        System.out.println("Executing: " + command);

        Map attributesMap;
        Map<String, String> opResult = new HashMap<>();

        //Extract attributes json into attributesMap
        try {
            attributesMap = gson.fromJson(attributes, Map.class);
        } catch (Exception ex) {
            opResult.put("Result", "Error: The attributes are not a json file.");
            //serverOut.println(gson.toJson(opResult));
            return;
        }

        /*
        //Extract command from map
        String command = (String) attributesMap.get("command");
        if (command == null) {
            opResult.put("Result", "Error: Command not valid.");
            //serverOut.println(gson.toJson(opResult));
            return;
        } else {
            System.out.println("Command type: " + command);
            //serverOut.println("Received: " + command);
        }
         */

        //Call method whose name is in 'command' and save the result in 'opResult'
        Method commandMethod = null;
        try {
            commandMethod = Controller.class.getDeclaredMethod(command, Map.class);
        } catch (SecurityException | NoSuchMethodException ex) {
            opResult.put("Result", "Error: Command not valid.");
            //serverOut.println(gson.toJson(opResult));
            return;
        }
        try {
            opResult.put("Result", (String) commandMethod.invoke(this, attributesMap));
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            opResult.put("Result", "Error: " + ex.getMessage());
            //serverOut.println(gson.toJson(opResult));
            return;
        }

        //serverOut.println(gson.toJson(opResult));
    }

    //PRIVATE METHODS (they are called inside run() with invoke() method)

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String chooseBonusResourceType(Map map) {
        System.out.println("Calling chooseBonusResourceType() on Game");
        String result;

        try {
            Resource resource = extractResource(map.get("resource"));
            int quantity = extractInt(map.get("quantity"));
            game.chooseBonusResourceType(resource, quantity);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String chooseLeaderCard(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            game.chooseLeaderCard(number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String playLeaderCard(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            game.playLeaderCard(number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String discardLeaderCard(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            game.discardLeaderCard(number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String selectMarketRow(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            game.selectMarketRow(number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String selectMarketColumn(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            game.selectMarketColumn(number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String sendResourceToDepot(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            Resource resource = extractResource(map.get("resource"));
            int quantity = extractInt(map.get("quantity"));
            game.sendResourceToDepot(number, resource, quantity);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String chooseMarbleConversion(Map map) {
        String result;

        try {
            Resource resource = extractResource(map.get("resource"));
            int quantity = extractInt(map.get("quantity"));
            game.chooseMarbleConversion(resource, quantity);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String swapDepotContent(Map map) {
        String result;

        try {
            int[] depots = extractIntArray(map.get("depots"));
            game.swapDepotContent(depots[0], depots[1]);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String moveDepotContent(Map map) {
        String result;

        try {
            int[] depots = extractIntArray(map.get("depots"));
            Resource resource = extractResource(map.get("resource"));
            int quantity = extractInt(map.get("quantity"));
            game.moveDepotContent(depots[0], depots[1], resource, quantity);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String takeDevelopmentCard(Map map) {
        String result;

        try {
            CardColor color = extractColor(map.get("color"));
            int level = extractInt(map.get("level"));
            int number = extractInt(map.get("number"));
            game.takeDevelopmentCard(color, level, number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String selectProduction(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            game.selectProduction(number);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String resetProductionChoice(Map map) {
        String result;

        try {
            game.resetProductionChoice();
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String confirmProductionChoice(Map map) {
        String result;

        try {
            game.confirmProductionChoice();
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String chooseJollyInput(Map map) {
        String result;

        try {
            Resource resource = extractResource(map.get("resource"));
            game.chooseJollyInput(resource);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String chooseJollyOutput(Map map) {
        String result;

        try {
            Resource resource = extractResource(map.get("resource"));
            game.chooseJollyOutput(resource);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String payFromWarehouse(Map map) {
        String result;

        try {
            int number = extractInt(map.get("number"));
            Resource resource = extractResource(map.get("resource"));
            int quantity = extractInt(map.get("quantity"));
            game.payFromWarehouse(number, resource, quantity);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String payFromStrongbox(Map map) {
        String result;

        try {
            Resource resource = extractResource(map.get("resource"));
            int quantity = extractInt(map.get("quantity"));
            game.payFromStrongbox(resource, quantity);
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     * @return a string containing the result of the operation, to be forwarded to the player
     */
    private String endTurn(Map map) {
        String result;

        try {
            game.endTurn();
            result = "Success";
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }

        return result;
    }

    //EXTRACTORS

    /**
     * Converts a Double received as an Object into an int
     *
     * @param obj the object to be converted
     * @return the resulting int
     */
    private int extractInt(Object obj) {
        if (obj == null || !(obj instanceof Double))
            throw new ParametersNotValidException();
        return ((Double) obj).intValue();
    }

    /**
     * Converts a String received as an Object into a Resource
     *
     * @param obj the object to be converted
     * @return the resulting Resource
     */
    private Resource extractResource(Object obj) {
        if (obj == null)
            throw new ParametersNotValidException();
        return (ResourceType.valueOf((String) obj)).toResource();
    }

    /**
     * Converts a String received as an Object into a CardColor
     *
     * @param obj the object to be converted
     * @return the resulting CardColor
     */
    private CardColor extractColor(Object obj) {
        if (obj == null)
            throw new ParametersNotValidException();
        return CardColor.valueOf((String) obj);
    }

    /**
     * Converts an array of Doubles received as an Object into an array of int
     *
     * @param obj the object to be converted
     * @return the resulting int[]
     */
    private int[] extractIntArray(Object obj) {
        if (obj == null)
            throw new ParametersNotValidException();
        return ((ArrayList<Double>) obj).stream().mapToInt(Double::intValue).toArray();
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", attributes='" + attributes + '\'' +
                '}';
    }
}
