package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.resource.ResourceType;
import model.storage.UnlimitedStorage;
import network.ServerMessageType;
import server.GameController;

import static model.resource.ResourceType.*;

/**
 * Class used to serialize an UnlimitedStorage object (in particular a WaitingRoom), send it over the network and store its information in the client
 */
public class WaitingRoomBean implements Observer, PlayerBean {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Represents the player's username
     */
    private final String username;
    /**
     * Hardcoded array of all the storable ResourceTypes
     */
    private final ResourceType[] type = {COIN, SERVANT, SHIELD, STONE};
    /**
     * Represents the quantity of each ResourceType present in the WaitingRoom
     */
    private final int[] quantity = new int[type.length];

    // CONSTRUCTOR

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     * @param username   the owner's username
     */
    public WaitingRoomBean(GameController controller, String username) {
        this.controller = controller;
        this.username = username;
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        UnlimitedStorage waitingRoom = (UnlimitedStorage) observable;
        setQuantityFromWaitingRoom(waitingRoom);

        controller.broadcastMessage(ServerMessageType.WAITINGROOM, gson.toJson(this));
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, ServerMessageType.WAITINGROOM, gson.toJson(this));
    }

    // PRINTING METHODS

    /**
     * This method is used to print only one line of the WaitingRoom so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line != 1)
            throw new ParametersNotValidException();

        String content = "";

        //Row 1
        for (int i = 0; i < type.length; i++) {
            content += " " + type[i].iconPrint() + " x " + quantity[i] + "  ";
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

        String result = Color.HEADER + username + "'s WaitingRoom:\n" + Color.RESET;

        result += printLine(1) +
                "\n";

        return result;
    }

    // GETTERS

    /**
     * Getter for the waiting room's player's username
     *
     * @return the player's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the resources that can be stored in the waiting room
     *
     * @return a ResourceType array of the resources that can be stored
     */
    public ResourceType[] getType() {
        return type.clone();
    }

    /**
     * Getter for the quantity of each resource stored in strongbox
     *
     * @return an int array of the quantity stored for each resource
     */
    public int[] getQuantity() {
        return quantity.clone();
    }

    // SETTERS

    /**
     * Sets the quantities for each resource type in waiting room
     *
     * @param waitingRoom the object to take the information from
     */
    private void setQuantityFromWaitingRoom(UnlimitedStorage waitingRoom) {
        for (int i = 0; i < type.length; i++) {
            quantity[i] = waitingRoom.getNumOfResource(type[i]);
        }
    }
}

