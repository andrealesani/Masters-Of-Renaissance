package network;

import network.MessageType;

/**
 * This class wraps a message that the server needs to send to a client or vice versa and adds a String that specifies the type of message wrapped.
 * In this way, when a client or server receives the message it can first read the message type and then decide  which operation to carry out
 */
public class MessageWrapper {
    /**
     * The type of message that is being sent.
     */
    private final MessageType type;
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
    public MessageWrapper(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    // GETTERS

    /**
     * Getter for the message's type
     *
     * @return the message's type
     */
    public MessageType getType() {
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
