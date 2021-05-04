package network.beans;

import com.google.gson.Gson;
import model.Observer;
import model.ResourceType;
import model.storage.UnlimitedStorage;

import static model.ResourceType.*;

public class StrongboxBean implements Observer {
    private final String username;
    private final ResourceType[] type = {COIN, SERVANT, SHIELD, STONE};
    private final int[] quantity = new int[type.length];

    // CONSTRUCTOR

    public StrongboxBean(String username) {
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

        BeanWrapper beanWrapper = new BeanWrapper(BeanType.STRONGBOX, gson.toJson(this));

        // TODO ask to the Controller to be sent to the clients
    }
}
