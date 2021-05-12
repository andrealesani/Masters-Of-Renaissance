package network.beans;

import com.google.gson.Gson;
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

    @Override
    public String toString() {
        return "\u001B[32;1m" + username + "'s Strongbox:\u001B[0m\n" +
                "   type: " + Arrays.toString(type) + "\n" +
                "   quantity: " + Arrays.toString(quantity) + "\n";
    }
}
