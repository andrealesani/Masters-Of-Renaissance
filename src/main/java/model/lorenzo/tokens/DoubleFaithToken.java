package model.lorenzo.tokens;

import model.lorenzo.Lorenzo;

/**
 * Represents the token that increases Lorenzo's faith by 2 points
 */
public class DoubleFaithToken implements ActionToken {
    /**
     * This attribute stores the token's owning a.i.
     */
    private final Lorenzo lorenzo;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param lorenzo reference to Lorenzo instance
     */
    public DoubleFaithToken(Lorenzo lorenzo) {
        this.lorenzo = lorenzo;
    }

    //PUBLIC METHODS

    /**
     * Standard method in the interface that calls the class-specific method doubleIncrease()
     */
    public void doAction() {
        doubleIncrease();
    }

    //PRIVATE METHODS
    /**
     * Increases Lorenzo's faith by 2 points
     */
    private void doubleIncrease() {
        lorenzo.addFaith(2);
    }
}
