package network.beans;

/**
 * This class is supposed to be created and used by the Controller. It wraps a message that the server needs to send
 * to a client and adds a String that specifies the type of message wrapped. In this way, when a client receives the message
 * it can first read the message type and then decide the object type to store it in
 */
public class MessageWrapper {
    /**
     * The type of message that is being sent.
     */
    private final MessageType type;
    /**
     * The message to be sent
     */
    private final String jsonMessage;

    public MessageWrapper(MessageType type, String jsonMessage) {
        this.type = type;
        this.jsonMessage = jsonMessage;
    }

    // GETTERS

    public MessageType getType() {
        return type;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }
}
