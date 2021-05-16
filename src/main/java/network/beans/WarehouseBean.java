package network.beans;

import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.ResourceType;
import model.storage.Warehouse;
import network.GameController;

import java.util.Arrays;


public class WarehouseBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    private final String username;
    private final int basicDepotNum;
    private ResourceType[] depotType;
    private int[] depotQuantity;

    // CONSTRUCTOR

    public WarehouseBean(GameController controller, String username, int basicDepotNum) {
        this.controller = controller;
        this.username = username;
        this.basicDepotNum = basicDepotNum;
    }

    // GETTERS

    public String getUsername() {
        return username;
    }

    public ResourceType[] getDepotType() {
        return depotType;
    }

    public int[] getDepotQuantity() {
        return depotQuantity;
    }

    public int getBasicDepotNum() {
        return basicDepotNum;
    }

    // SETTERS

    public void setDepotsFromWarehouse(Warehouse warehouse) {
        depotType = new ResourceType[warehouse.getNumOfDepots()];
        depotQuantity = new int[depotType.length];

        for (int i = 0; i < warehouse.getNumOfDepots(); i++) {
            if (warehouse.getDepot(i + 1).getStoredResources().size() > 0)
                depotType[i] = warehouse.getDepot(i + 1).getStoredResources().get(0);
            depotQuantity[i] = warehouse.getDepot(i + 1).getNumOfResource(depotType[i]);
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        Warehouse warehouse = (Warehouse) observable;
        setDepotsFromWarehouse(warehouse);

        controller.broadcastMessage(MessageType.WAREHOUSE, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.WAREHOUSE, gson.toJson(this));
    }

    @Override
    public String toString() {
        String content = "";
        for (int i = 0; i < depotType.length; i++) {
            if (depotType[i] != null)
                content += " " + depotType[i] + " " + depotQuantity[i];
            else
                content += Color.ORANGE_FG + " EMPTY" + Color.DEFAULT;
            if (i < basicDepotNum)
                content += " [max: " + (i + 1) + "] ";
            else
                content += " [max: 2] ";
        }
        return Color.HEADER + username + "'s Warehouse:\n" + Color.DEFAULT +
                " basicDepotNum: " + basicDepotNum + "\n" + content + "\n";
    }
}
