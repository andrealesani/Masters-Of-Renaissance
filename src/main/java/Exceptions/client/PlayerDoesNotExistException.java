package Exceptions.client;

import model.resource.ResourceType;

/**
 * This Exception is thrown when a ClientView method is called in regards to a player whose username is not present in the ClientView
 */
public class PlayerDoesNotExistException extends Exception {
    private final String username;

    /**
     * Constructor
     *
     * @param username the username of the player which does not exist
     */
    public PlayerDoesNotExistException(String username) {
        this.username = username;

    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Player " + username + " is not present in the game.");
    }
}
