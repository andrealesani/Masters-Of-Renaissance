package model.lorenzo.tokens;

import network.beans.TokenType;

/**
 * This abstract class represents an action token for the game's lorenzo a.i.
 */
public abstract class ActionToken {
    /**
     * TokenType associated to the specific ActionToken
     */
    private final TokenType type;

    // CONSTRUCTOR

    public ActionToken(TokenType type) {
        this.type = type;
    }

    //PUBLIC METHODS

    /**
     * Activates the specific action of the token that has been drawn
     */
    public void doAction(){};

    // GETTERS

    public TokenType getType() {
        return type;
    }
}
