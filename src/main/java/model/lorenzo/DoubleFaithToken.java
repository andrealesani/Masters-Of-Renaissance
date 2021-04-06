package model.lorenzo;

import model.CardTable;

/**
 * Represents the token that increases Lorenzo's faith by 2 points
 */
public class DoubleFaithToken implements ActionToken {
    private final Lorenzo lorenzo;

    /**
     * Constructor
     *
     * @param lorenzo reference to Lorenzo instance
     */
    public DoubleFaithToken(Lorenzo lorenzo) {
        this.lorenzo = lorenzo;
    }

    /**
     * Increases Lorenzo's faith by 2 points
     */
    private void doubleIncrease() {
        lorenzo.faithIncrease();
        lorenzo.faithIncrease();
    }

    /**
     * Standard method in the interface that calls the class-specific method doubleIncrease()
     */
    public void doAction() {
        doubleIncrease();
    }
}
