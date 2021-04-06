package model.lorenzo;

import model.CardTable;

/**
 * Represents the token that increases Lorenzo's faith by 1 and then shuffles the entire action tokens deck
 */
public class SingleFaithShuffleToken implements ActionToken {
    private final Lorenzo lorenzo;

    /**
     * Constructor
     *
     * @param lorenzo reference to Lorenzo instance
     */
    public SingleFaithShuffleToken(Lorenzo lorenzo) {
        this.lorenzo = lorenzo;
    }

    /**
     * Increases Lorenzo's faith by 1 and then shuffles the entire action tokens deck
     */
    private void singleIncreaseShuffle() {
        lorenzo.faithIncrease();
        lorenzo.shuffleDeck();
    }

    /**
     * Standard method in the interface that calls the class-specific method singleIncreaseShuffle()
     */
    public void doAction() {
        singleIncreaseShuffle();
    }
}
