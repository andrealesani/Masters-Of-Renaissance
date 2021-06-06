package network.beans;

import com.google.gson.Gson;
import model.Observer;
import model.Production;
import model.ProductionHandler;
import model.resource.Resource;
import model.resource.ResourceType;
import network.MessageType;
import server.GameController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductionHandlerBean implements Observer {
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
    private Map<ResourceType, Integer> input;
    /**
     * Map that represents the output resources (the ones the player will earn if he activates the selected productions)
     */
    private Map<ResourceType, Integer> output;

    // CONSTRUCTOR

    /**
     * Server constructor
     *
     * @param controller to which the bean notifies changes
     */
    public ProductionHandlerBean(GameController controller, String username) {
        this.controller = controller;
        this.username = username;

        input = new HashMap<>();
        output = new HashMap<>();
    }

    /**
     * Client constructor, it doesn't need reference to the controller
     */
    public ProductionHandlerBean() {
        this.controller = null;

        input = new HashMap<>();
        output = new HashMap<>();
    }

    // GETTERS

    public Map<ResourceType, Integer> getInput() {
        return input;
    }

    public Map<ResourceType, Integer> getOutput() {
        return output;
    }

    public String getUsername() { return username; }

    public int[] getProductions() {
        return productions;
    }

    public boolean[] getActiveProductions() {
        return activeProductions;
    }

    // SETTERS

    public void setInputFromPH(ProductionHandler productionHandler) {
        input.clear();
        for (ResourceType resourceType : productionHandler.getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList()))
            input.merge(resourceType, 1, Integer::sum);
    }

    public void setOutputFromPH(ProductionHandler productionHandler) {
        output.clear();
        for (ResourceType resourceType : productionHandler.getCurrentOutput().stream().map(Resource::getType).collect(Collectors.toList()))
            output.merge(resourceType, 1, Integer::sum);
    }

    public void setProductionsFromPH(ProductionHandler productionHandler) {
        productions = new int[productionHandler.getProductions().size()];
        activeProductions = new boolean[productions.length];
        for (int i = 0; i < productions.length; i++) {
            productions[i] = productionHandler.getProductions().get(i).getId();
            if (productionHandler.getProductions().get(i).isSelectedByHandler())
                activeProductions[i] = true;
            else
                activeProductions[i] = false;
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        ProductionHandler pH = (ProductionHandler) observable;

        setInputFromPH(pH);
        setOutputFromPH(pH);
        setProductionsFromPH(pH);

        controller.broadcastMessage(MessageType.PRODUCTIONHANDLER, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.PRODUCTIONHANDLER, gson.toJson(this));
    }
}
