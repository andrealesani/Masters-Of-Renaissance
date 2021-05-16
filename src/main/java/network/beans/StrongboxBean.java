package network.beans;

import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.ResourceType;
import model.storage.UnlimitedStorage;
import network.GameController;

import java.util.Arrays;

import static model.ResourceType.*;

public class StrongboxBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    private final String username;
    private final ResourceType[] type = {COIN, SERVANT, SHIELD, STONE};
    private final int[] quantity = new int[type.length];

    // CONSTRUCTOR

    public StrongboxBean(GameController controller, String username) {
        this.controller = controller;
        this.username = username;
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

    // SETTERS

    private void setQuantityFromStrongbox(UnlimitedStorage strongbox) {
        for(int i = 0; i < type.length; i++) {
            quantity[i] = strongbox.getNumOfResource(type[i]);
        }
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

    @Override
    public String toString() {
        String content = "";
        for (int i = 0; i < type.length; i++) {
            content += " " + type[i] + ": " + quantity[i] + "  ";
        }
        return Color.HEADER + username + "'s Strongbox:\n" + Color.DEFAULT + content + "\n";
    }
}
