package network.beans;

/**
 * This interface indicates that the implementing class is used as a bean in the server - client communication,
 * and is the type of bean of which a version exists for every player in the game.
 */
public interface PlayerBean {
    /**
     * Returns the username of the player the bean belongs to
     *
     * @return the username of the player
     */
    String getUsername();
}
