package network.beans;

import com.google.gson.Gson;
import model.Observer;
import model.ProductionHandler;
import model.resource.Resource;
import model.resource.ResourceType;
import network.MessageType;
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

        emptyInput();
        emptyOutput();
    }

    /**
     * Client constructor, it doesn't need reference to the controller
     */
    public ProductionHandlerBean() {
        this.controller = null;

        input = new HashMap<>();
        output = new HashMap<>();

        emptyInput();
        emptyOutput();
    }

    // GETTERS

    public Map<ResourceType, Integer> getInput() {
        return input;
    }

    public Map<ResourceType, Integer> getOutput() {
        return output;
    }

    @Override
    public String getUsername() { return username; }

    public int[] getProductions() {
        return productions;
    }

    public boolean[] getActiveProductions() {
        return activeProductions;
    }

    // SETTERS

    /**
     * Sets current input reading data from the given ProductionHandler
     *
     * @param productionHandler object to take the information from
     */
    public void setInputFromPH(ProductionHandler productionHandler) {
        emptyInput();
        for (ResourceType resourceType : productionHandler.getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList()))
            input.merge(resourceType, 1, Integer::sum);
    }

    /**
     * Sets current output reading data from the given ProductionHandler
     *
     * @param productionHandler object to take the information from
     */
    public void setOutputFromPH(ProductionHandler productionHandler) {
        emptyOutput();
        for (ResourceType resourceType : productionHandler.getCurrentOutput().stream().map(Resource::getType).collect(Collectors.toList()))
            output.merge(resourceType, 1, Integer::sum);
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
            if (productionHandler.getProductions().get(i).isSelectedByHandler())
                activeProductions[i] = true;
            else
                activeProductions[i] = false;
        }
    }

    // PRIVATE METHODS

    /**
     * Sets all the input resources to 0
     */
    private void emptyInput() {
        input.put(ResourceType.COIN, 0);
        input.put(ResourceType.SERVANT, 0);
        input.put(ResourceType.SHIELD, 0);
        input.put(ResourceType.STONE, 0);
        input.put(ResourceType.JOLLY, 0);
    }

    /**
     * Sets all the output resources to 0
     */
    private void emptyOutput() {
        output.put(ResourceType.COIN, 0);
        output.put(ResourceType.SERVANT, 0);
        output.put(ResourceType.SHIELD, 0);
        output.put(ResourceType.STONE, 0);
        output.put(ResourceType.FAITH, 0);
        output.put(ResourceType.JOLLY, 0);
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

    // TODO stampa a CLI dei ProductionHandlers
}
