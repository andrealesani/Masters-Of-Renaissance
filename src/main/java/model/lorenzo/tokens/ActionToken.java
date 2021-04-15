package model.lorenzo.tokens;

/**
 * This interfaces indicates that the implementing class can be used as an action token for the game's lorenzo a.i.
 */
public interface ActionToken {
    //PUBLIC METHODS
    /**
     * Activates the specific action of the token that has been drawn
     */
    void doAction();
}
