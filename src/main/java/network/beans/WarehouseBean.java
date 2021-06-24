package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.resource.ResourceType;
import model.storage.ResourceDepot;
import model.storage.Warehouse;
import network.ServerMessageType;
import server.GameController;

/**
 * Class used to serialize a Warehouse object, send it over the network and store its information in the client
 */
public class WarehouseBean implements Observer, PlayerBean {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Represents the player's username
     */
    private final String username;
    /**
     * Represents the number of basic depots in the Warehouse
     */
    private final int basicDepotNum;
    /**
     * Represents the storable ResourceType for each depot
     */
    private ResourceType[] depotType;
    /**
     * Represents the quantity of Resources in every depot
     */
    private int[] depotQuantity;
    /**
     * Represents the maximum capacity of each depot
     */
    private int[] depotSizes;

    // CONSTRUCTOR

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     */
    public WarehouseBean(GameController controller, String username, int basicDepotNum) {
        this.controller = controller;
        this.username = username;
        this.basicDepotNum = basicDepotNum;
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        Warehouse warehouse = (Warehouse) observable;
        setDepotsFromWarehouse(warehouse);

        controller.broadcastMessage(ServerMessageType.WAREHOUSE, gson.toJson(this));
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, ServerMessageType.WAREHOUSE, gson.toJson(this));
    }

    //PRINTING METHODS

    /**
     * This method is used to print only one line of the Warehouse so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line != 1)
            throw new ParametersNotValidException();

        String content = "";

        content += drawSlots();

        return content;
    }

    /**
     * Prints a String representation of the bean's data
     *
     * @return the String representation
     */
    @Override
    public String toString() {

        String result = Color.HEADER + username + "'s warehouse:\n" + Color.RESET;

        result +=   printLine(1) +
                    "\n";

        return result;
    }

    //PRIVATE PRINTING METHODS

    /**
     * Returns a String representation of the warehouse's depots
     *
     * @return the String representation of the depots
     */
    private String drawSlots() {
        String content = "";

        for (int i = 0; i < depotType.length; i++) {

            if (i == basicDepotNum)
                content += " |";

            content += " " + (i+1);
            content += ".[ ";
            int quantity = depotQuantity[i];

            //Prints either the resource or a gray square
            for (int j = 0; j < depotSizes[i]; j++) {

                if (quantity > 0) {
                    content += depotType[i].iconPrint() + " ";
                    quantity--;
                } else {
                    content += Color.GREY_LIGHT_FG + "■ " + Color.RESET;
                }

                //For leader depots prints the type of resource they can store
                if (i >= basicDepotNum)
                    content += "(" + depotType[i].iconPrint() + ")";
            }

            content += "]";
        }

        return content;
    }

    // GETTERS

    /**
     * Getter for the strongbox's player's username
     *
     * @return the player's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the resources that are store (or can be stored for leader depots) in each depot
     *
     * @return a ResourceType array of the resources that can be stored
     */
    public ResourceType[] getDepotType() {
        return depotType.clone();
    }

    /**
     * Getter for the quantity of resources stored in each depot
     *
     * @return an int array of the quantity of resource stored for each depot
     */
    public int[] getDepotQuantity() {
        return depotQuantity.clone();
    }

    /**
     * Getter for the size of each depot
     *
     * @return an int array of the size of each depot
     */
    public int[] getDepotSizes() {
        return depotSizes.clone();
    }

    /**
     * Getter for the number of warehouse depots that are basic depots
     *
     * @return the number of basic depots
     */
    public int getBasicDepotNum() {
        return basicDepotNum;
    }

    // SETTERS

    /**
     * Sets the depot's information
     *
     * @param warehouse the object to take the information from
     */
    public void setDepotsFromWarehouse(Warehouse warehouse) {
        depotType = new ResourceType[warehouse.getNumOfDepots()];
        depotQuantity = new int[depotType.length];
        depotSizes = new int[depotType.length];

        for (int i = 0; i < warehouse.getNumOfDepots(); i++) {
            ResourceDepot depot = warehouse.getDepot(i + 1);

            depotType[i] = depot.getAcceptedResource();
            depotQuantity[i] = depot.getNumOfResource(depotType[i]);
            depotSizes[i] = depot.getSize();
        }
    }
}
