package model.lorenzo.tokens;

import network.beans.LorenzoTokenType;

/**
 * This abstract class represents an action token for the game's lorenzo a.i.
 */
public abstract class ActionToken {
    /**
     * LorenzoTokenType associated to the specific ActionToken
     */
    private final LorenzoTokenType type;

    // CONSTRUCTOR

    public ActionToken(LorenzoTokenType type) {
        this.type = type;
    }

    //PUBLIC METHODS

    /**
     * Activates the specific action of the token that has been drawn
     */
    public void doAction(){};

    // GETTERS

    public LorenzoTokenType getType() {
        return type;
    }
}
