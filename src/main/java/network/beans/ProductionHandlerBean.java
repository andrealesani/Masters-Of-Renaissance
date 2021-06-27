package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.ProductionHandler;
import model.resource.Resource;
import model.resource.ResourceType;
import network.ServerMessageType;
import server.GameController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class used to serialize a ProductionHandler object
 */
public class ProductionHandlerBean implements Observer, PlayerBean {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Represents the player's username
     */
    private String username;
    /**
     * Holds the IDs of the Productions available to the player
     */
    private int[] productions;
    /**
     * Holds booleans that tell if the Production is that position of the array has been chosen for activation
     */
    private boolean[] activeProductions;
    /**
     * Map that represents the input resources (the ones the player will have to pay if he activates the selected productions)
     */
    private final Map<ResourceType, Integer> input;
    /**
     * Map that represents the output resources (the ones the player will earn if he activates the selected productions)
     */
    private final Map<ResourceType, Integer> output;

    // CONSTRUCTOR

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     * @param username   the owner's username
     */
    public ProductionHandlerBean(GameController controller, String username) {
        this.controller = controller;
        this.username = username;

        input = new HashMap<>();
        output = new HashMap<>();
    }

    // PRIVATE METHODS

    /**
     * Sets the value for all the resources that can be present in input to zero for the given Map
     *
     * @param input the Map to set
     */
    private void emptyInput(Map<ResourceType, Integer> input) {
        input.put(ResourceType.COIN, 0);
        input.put(ResourceType.SERVANT, 0);
        input.put(ResourceType.SHIELD, 0);
        input.put(ResourceType.STONE, 0);
        input.put(ResourceType.JOLLY, 0);
    }

    /**
     * Sets the value for all the resources that can be present in output to zero for the given Map
     *
     * @param output the Map to set
     */
    private void emptyOutput(Map<ResourceType, Integer> output) {
        output.put(ResourceType.COIN, 0);
        output.put(ResourceType.SERVANT, 0);
        output.put(ResourceType.SHIELD, 0);
        output.put(ResourceType.STONE, 0);
        output.put(ResourceType.FAITH, 0);
        output.put(ResourceType.JOLLY, 0);
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        ProductionHandler pH = (ProductionHandler) observable;

        setInputFromPH(pH);
        setOutputFromPH(pH);
        setProductionsFromPH(pH);

        controller.broadcastMessage(ServerMessageType.PRODUCTIONHANDLER, gson.toJson(this));
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, ServerMessageType.PRODUCTIONHANDLER, gson.toJson(this));
    }

    // PRINTING METHODS

    // TODO stampa a CLI dei ProductionHandlers

    /**
     * This method is used to print only one line of the Market so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line < 1 || line > 3)
            throw new ParametersNotValidException();

        String content = "";

        switch (line) {
            case 1 -> content += " " + drawProductions();
            case 2 -> content += " Current input: " + drawInput();
            case 3 -> content += " Current output: " + drawOutput();
        }

        return content;
    }

    /**
     * Prints a String representation of the bean's data
     *
     * @return the String representation
     */
    @Override
    public String toString() {

        String result = Color.HEADER + "Available productions: " + Color.RESET;

        result += printLine(1) +
                "\n" +
                printLine(2) +
                "\n" +
                printLine(3) +
                "\n";

        return result;
    }

    // PRIVATE DRAWING METHODS

    private String drawProductions() {
        String content = "";

        for (int i = 0; i < productions.length; i++) {
            if (activeProductions[i]) {
                content += Color.RESOURCE_STD;
            } else {
                content += Color.RESET;
            }
            content += "[" + productions[i] + "] ";
            content += Color.RESET;
        }

        return content;
    }

    private String drawInput() {
        String content = "";

        for (ResourceType resource : input.keySet()) {
            content += " " + resource.iconPrint() + " x " + input.get(resource) + "  ";
        }

        return content;
    }

    private String drawOutput() {
        String content = "";

        for (ResourceType resource : output.keySet()) {
            content += " " + resource.iconPrint() + " x " + output.get(resource) + "  ";
        }

        return content;
    }

    // GETTERS

    /**
     * Getter for the selected productions' input. Absent resources are set to zero
     *
     * @return a Map where the key set are the possible ResourceTypes and the values are their required amount for the input
     */
    public Map<ResourceType, Integer> getInput() {
        Map<ResourceType, Integer> result = new HashMap<>();

        emptyInput(result);
        for (ResourceType resource : input.keySet())
            result.put(resource, input.get(resource));

        return result;
    }

    /**
     * Getter for the selected productions' output. Absent resources are set to zero
     *
     * @return a Map where the key set are the possible ResourceTypes and the values are their amount in output
     */
    public Map<ResourceType, Integer> getOutput() {
        Map<ResourceType, Integer> result = new HashMap<>();

        emptyOutput(result);
        for (ResourceType resource : output.keySet())
            result.put(resource, output.get(resource));

        return result;
    }

    /**
     * Getter for the production handler's available productions
     *
     * @return an array of int of the productions' card id
     */
    public int[] getProductions() {
        return productions.clone();
    }

    /**
     * Getter for the activation status of the productions
     *
     * @return an array of boolean where a position is true if the corresponding production is active
     */
    public boolean[] getActiveProductions() {
        return activeProductions.clone();
    }

    /**
     * Getter for the production handler's player's username
     *
     * @return the player's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    // SETTERS

    /**
     * Sets current input reading data from the given ProductionHandler
     *
     * @param productionHandler object to take the information from
     */
    public void setInputFromPH(ProductionHandler productionHandler) {
        input.clear();
        for (ResourceType resourceType : productionHandler.getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList())) {
            input.merge(resourceType, 1, Integer::sum);
        }
    }

    /**
     * Sets current output reading data from the given ProductionHandler
     *
     * @param productionHandler object to take the information from
     */
    public void setOutputFromPH(ProductionHandler productionHandler) {
        output.clear();
        for (ResourceType resourceType : productionHandler.getCurrentOutput().stream().map(Resource::getType).collect(Collectors.toList())) {
            output.merge(resourceType, 1, Integer::sum);
        }
    }

    /**
     * Sets player's Productions and selected ones into 'productions' and 'activeProductions' arrays reading data
     * from the given ProductionHandler
     *
     * @param productionHandler object to take the information from
     */
    public void setProductionsFromPH(ProductionHandler productionHandler) {
        productions = new int[productionHandler.getProductions().size()];
        activeProductions = new boolean[productions.length];
        for (int i = 0; i < productions.length; i++) {
            productions[i] = productionHandler.getProductions().get(i).getId();
            activeProductions[i] = productionHandler.getProductions().get(i).isSelectedByHandler();
        }
    }
}
