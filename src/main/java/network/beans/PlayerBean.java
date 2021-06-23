package network.beans;

/**
 * This interface indicates that the implementing class is used as a bean in the server - client communication,
 * and is the type of bean of which a version exists for every player in the game.
 */
public interface PlayerBean {
    /**
     * Getter for the bean's player's username
     *
     * @return the player's username
     */
    String getUsername();
}
