package network.beans;

import network.ServerMessageType;

/**
 * This class is supposed to be created and used by the Controller. It wraps a message that the server needs to send
 * to a client and adds a String that specifies the type of message wrapped. In this way, when a client receives the message
 * it can first read the message type and then decide the object type to store it in (or to just show the information
 * without storing it i.e. an INFO or an ERROR)
 */
public class MessageWrapper {
    /**
     * The type of message that is being sent.
     */
    private final ServerMessageType type;
    /**
     * The message to be sent
     */
    private final String message;

    /**
     * Constructor
     *
     * @param type of the message being wrapped
     * @param message the actual message being sent
     */
    public MessageWrapper(ServerMessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    // GETTERS

    /**
     * Getter for the message's type
     *
     * @return the message's type
     */
    public ServerMessageType getType() {
        return type;
    }

    /**
     * Getter for the message's content
     *
     * @return a String representing the serialized message
     */
    public String getMessage() {
        return message;
    }
}
