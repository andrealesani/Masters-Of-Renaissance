package network.beans;

import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.resource.ResourceType;
import model.storage.UnlimitedStorage;
import server.GameController;

import static model.resource.ResourceType.*;

public class StrongboxBean implements Observer {
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
     * Represents the quantity of each ResourceType present in the Strongbox
     */
    private final int[] quantity = new int[type.length];

    // CONSTRUCTOR

    public StrongboxBean(GameController controller, String username) {
        this.controller = controller;
        this.username = username;
    }

    // SETTERS

    private void setQuantityFromStrongbox(UnlimitedStorage strongbox) {
        for(int i = 0; i < type.length; i++) {
            quantity[i] = strongbox.getNumOfResource(type[i]);
        }
    }

    // GETTERS

    public String getUsername() {
        return username;
    }

    public ResourceType[] getType() {
        return type;
    }

    public int[] getQuantity() {
        return quantity;
    }


    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        UnlimitedStorage strongbox = (UnlimitedStorage) observable;
        setQuantityFromStrongbox(strongbox);

        controller.broadcastMessage(MessageType.STRONGBOX, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.STRONGBOX, gson.toJson(this));
    }

    public String printLine(int line) {
        line --;
        String content = "";
        if (line == 0) {
            for (int i = 0; i < type.length; i++) {
                content += " " + type[i].iconPrint() + " x " + quantity[i] + "  ";
            }
        }

        return content;
    }

    @Override
    public String toString() {
        String content = "";
        for (int i = 0; i < type.length; i++) {
            content += " " + type[i].iconPrint() + " x " + quantity[i] + "  ";
        }
        return Color.HEADER + username + "'s Strongbox:\n" + Color.RESET + content + "\n";
    }
}
