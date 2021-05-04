package network.beans;

/**
 * This class is supposed to be created and used by the Controller. It wraps a bean that the server needs to send
 * to a client and adds a String that specifies the type of bean wrapped. In this way, when a client receives the message
 * it can first read the bean type and then decide the object type to store it in
 */
public class BeanWrapper {
    /**
     * The type of message that is being sent.
     */
    private final BeanType type;
    /**
     * The bean to be sent
     */
    private final String jsonBean;

    public BeanWrapper(BeanType type, String jsonBean) {
        this.type = type;
        this.jsonBean = jsonBean;
    }

    // GETTERS

    public BeanType getType() {
        return type;
    }

    public String getJsonBean() {
        return jsonBean;
    }
}
