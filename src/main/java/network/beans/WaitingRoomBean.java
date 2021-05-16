package network.beans;

import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.ResourceType;
import model.storage.UnlimitedStorage;
import network.GameController;

import java.util.Arrays;

import static model.ResourceType.*;

public class WaitingRoomBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    private final String username;
    private final ResourceType[] type = {COIN, SERVANT, SHIELD, STONE};
    private final int[] quantity = new int[type.length];

    // CONSTRUCTOR

    public WaitingRoomBean(GameController controller, String username) {
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

    private void setQuantityFromStrongbox(UnlimitedStorage waitingRoom) {
        for(int i = 0; i < type.length; i++) {
            quantity[i] = waitingRoom.getNumOfResource(type[i]);
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        UnlimitedStorage waitingRoom = (UnlimitedStorage) observable;
        setQuantityFromStrongbox(waitingRoom);

        controller.broadcastMessage(MessageType.WAITINGROOM, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.WAITINGROOM, gson.toJson(this));
    }

    public String printLine(int line) {
        line --;
        String content = "";
        if (line == 0) {
            for (int i = 0; i < type.length; i++) {
                content += " " + type[i] + ": " + quantity[i] + "  ";
            }
        }

        return content;
    }

    @Override
    public String toString() {
        String content = "";
        for (int i = 0; i < type.length; i++) {
            content += " " + type[i] + ": " + quantity[i] + "  ";
        }
        return Color.HEADER + username + "'s WaitingRoom:\n" + Color.DEFAULT + content + "\n";
    }
}

