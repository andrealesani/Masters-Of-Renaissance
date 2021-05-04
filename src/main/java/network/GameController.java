package network;

import Exceptions.*;
import com.google.gson.Gson;
import model.CardColor;
import model.ResourceType;
import model.UserCommands;
import model.resource.Resource;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    final Gson gson;
    UserCommands game = null;

    //CONSTRUCTORS

    public GameController() {
        this.gson = new Gson();
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
            return;
        }

        //Extract command from map
        String command = (String) jsonMap.get("command");
        if (command == null) {
            opResult.put("Result", "Error");
            opResult.put("Message", "Command not valid.");
            serverOut.println(gson.toJson(opResult));
            return;
        } else {
            System.out.println("Command type: " + command);
        }

        //Call method the name of which is in 'command' and save the result in 'opResult'
        Method commandMethod = null;
        try {
            commandMethod = GameController.class.getDeclaredMethod(command, Map.class, Map.class);
        } catch (SecurityException | NoSuchMethodException ex) {
            opResult.put("Result", "Error");
            opResult.put("Message", "Command not valid.");
            serverOut.println(gson.toJson(opResult));
            return;
        }
        try {
            commandMethod.invoke(this, jsonMap, opResult);
            opResult.put("Result", "Success");
        } catch (Exception ex) {
            opResult.put("Result", "Error");
            opResult.put("Message", ex.getMessage());
            serverOut.println(gson.toJson(opResult));
            return;
        }

        serverOut.println(gson.toJson(opResult));
    }


    //PRIVATE METHODS

    //TODO make methods put error message in message key in map

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void chooseBonusResourceType(Map map) throws NotEnoughResourceException, WrongTurnPhaseException {

        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));
        game.chooseBonusResourceType(resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void chooseLeaderCard(Map map) throws WrongTurnPhaseException {

        int number = extractInt(map.get("number"));
        game.chooseLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void playLeaderCard(Map map) throws LeaderRequirementsNotMetException, WrongTurnPhaseException {
        int number = extractInt(map.get("number"));
        game.playLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void discardLeaderCard(Map map) throws LeaderIsActiveException, WrongTurnPhaseException {
        int number = extractInt(map.get("number"));
        game.discardLeaderCard(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void selectMarketRow(Map map) throws WrongTurnPhaseException {
        int number = extractInt(map.get("number"));
        game.selectMarketRow(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void selectMarketColumn(Map map) throws WrongTurnPhaseException {
        int number = extractInt(map.get("number"));
        game.selectMarketColumn(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void sendResourceToDepot(Map map) throws DepotNotPresentException, NotEnoughSpaceException, NotEnoughResourceException, WrongResourceInsertionException, WrongTurnPhaseException, BlockedResourceException {
        int number = extractInt(map.get("number"));
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));
        game.sendResourceToDepot(number, resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void chooseMarbleConversion(Map map) throws NotEnoughResourceException, ConversionNotAvailableException, WrongTurnPhaseException {
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));
        game.chooseMarbleConversion(resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void swapDepotContent(Map map) throws DepotNotPresentException, SwapNotValidException, WrongTurnPhaseException {
        int[] depots = extractIntArray(map.get("depots"));
        game.swapDepotContent(depots[0], depots[1]);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void moveDepotContent(Map map) throws DepotNotPresentException, NotEnoughSpaceException, NotEnoughResourceException, WrongTurnPhaseException, WrongResourceInsertionException, BlockedResourceException {
        int[] depots = extractIntArray(map.get("depots"));
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));
        game.moveDepotContent(depots[0], depots[1], resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void takeDevelopmentCard(Map map) throws SlotNotValidException, NotEnoughResourceException, EmptyDeckException, WrongTurnPhaseException {
        CardColor color = extractColor(map.get("color"));
        int level = extractInt(map.get("level"));
        int number = extractInt(map.get("number"));
        game.takeDevelopmentCard(color, level, number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void selectProduction(Map map) throws ProductionNotPresentException, WrongTurnPhaseException {
        int number = extractInt(map.get("number"));
        game.selectProduction(number);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void resetProductionChoice(Map map) throws WrongTurnPhaseException {
        game.resetProductionChoice();
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void confirmProductionChoice(Map map) throws NotEnoughResourceException, UnknownResourceException, WrongTurnPhaseException {
        game.confirmProductionChoice();
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void chooseJollyInput(Map map) throws ResourceNotPresentException, WrongTurnPhaseException {
        Resource resource = extractResource(map.get("resource"));
        game.chooseJollyInput(resource);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void chooseJollyOutput(Map map) throws ResourceNotPresentException, WrongTurnPhaseException {
        Resource resource = extractResource(map.get("resource"));
        game.chooseJollyOutput(resource);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void payFromWarehouse(Map map) throws DepotNotPresentException, NotEnoughResourceException, WrongTurnPhaseException {
        int number = extractInt(map.get("number"));
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));
        game.payFromWarehouse(number, resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void payFromStrongbox(Map map) throws NotEnoughResourceException, WrongTurnPhaseException {
        Resource resource = extractResource(map.get("resource"));
        int quantity = extractInt(map.get("quantity"));
        game.payFromStrongbox(resource, quantity);
    }

    /**
     * Invokes homonymous method on the Model's Game class
     *
     * @param map the message received from the player
     */
    private void endTurn(Map map) throws WrongTurnPhaseException {
        game.endTurn();
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
        if (obj == null || !(obj instanceof String))
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
        if (obj == null || !(obj instanceof String))
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
        if (obj == null || !(obj instanceof ArrayList))
            throw new ParametersNotValidException();
        return ((ArrayList<Double>) obj).stream().mapToInt(Double::intValue).toArray();
    }

}
