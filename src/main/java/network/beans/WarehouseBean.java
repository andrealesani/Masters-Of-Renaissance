package network.beans;

import com.google.gson.Gson;
import model.Observer;
import model.ResourceType;
import model.storage.Warehouse;


public class WarehouseBean implements Observer {
    private final String username;
    private final int basicDepotNum;
    private ResourceType[] depotType;
    private int[] depotQuantity;

    public WarehouseBean(String username, int basicDepotNum) {
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
            depotType[i] = warehouse.getDepot(i).getStoredResources().get(0);
            depotQuantity[i] = warehouse.getDepot(i).getStoredResources().size();
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        Warehouse warehouse = (Warehouse) observable;
        setDepotsFromWarehouse(warehouse);

        BeanWrapper beanWrapper = new BeanWrapper(BeanType.WAREHOUSE, gson.toJson(this));

        // TODO ask to the Controller to be sent to the clients
    }
}
