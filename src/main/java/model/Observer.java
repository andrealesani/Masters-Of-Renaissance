package model;

/**
 * This interface indicates that the implementing class can be an observer for changes in the state of Observable classes
 */
public interface Observer {
    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    void update(Object observable);

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    void updateSinglePlayer(String username);
}
