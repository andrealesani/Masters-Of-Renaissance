package it.polimi.ingsw.model.lorenzo.tokens;

/**
 * This abstract class represents an action token for the game's lorenzo a.i.
 */
public abstract class ActionToken {
    /**
     * LorenzoTokenType associated to the specific ActionToken
     */
    private final LorenzoTokenType type;

    // CONSTRUCTOR

    /**
     * Constructor
     *
     * @param type the token's type
     */
    public ActionToken(LorenzoTokenType type) {
        this.type = type;
    }

    //PUBLIC METHODS

    /**
     * Activates the specific action of the token that has been drawn
     */
    public void doAction(){};

    // GETTERS

    /**
     * Getter for the token's type
     *
     * @return the type of the token
     */
    public LorenzoTokenType getType() {
        return type;
    }
}
