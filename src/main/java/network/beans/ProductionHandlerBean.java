package network.beans;

import com.google.gson.Gson;
import model.Observer;
import model.ProductionHandler;
import model.resource.Resource;
import model.resource.ResourceType;
import network.MessageType;
import server.GameController;

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
    }

    /**
     * Client constructor, it doesn't need reference to the controller
     */
    public ProductionHandlerBean() {
        this.controller = null;
    }

    // GETTERS

    public Map<ResourceType, Integer> getInput() {
        return input;
    }

    public Map<ResourceType, Integer> getOutput() {
        return output;
    }

    public String getUsername() { return username; }

    // SETTERS

    public void setInputFromPH(ProductionHandler productionHandler) {
        for (ResourceType resourceType : productionHandler.getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList()))
            input.merge(resourceType, 1, Integer::sum);
    }

    public void setOutputFromPH(ProductionHandler productionHandler) {
        for (ResourceType resourceType : productionHandler.getCurrentOutput().stream().map(Resource::getType).collect(Collectors.toList()))
            output.merge(resourceType, 1, Integer::sum);
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        ProductionHandler pH = (ProductionHandler) observable;

        setInputFromPH(pH);
        setOutputFromPH(pH);

        controller.broadcastMessage(MessageType.PRODUCTIONHANDLER, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.PRODUCTIONHANDLER, gson.toJson(this));
    }
}
