package model.lorenzo;

import model.lorenzo.ActionToken;

import java.util.Collections;
import java.util.List;

/**
 * Contains all information and methods needed to play solo mode. Lorenzo is not meant to be considered as
 * a second player in the game but only as a series of actions done between the player's turns
 */
public class Lorenzo {
    private int faith;
    private List<ActionToken> activeDeck;
    private List<ActionToken> usedDeck;

    /**
     * Constructor
     */
    public Lorenzo() {
        faith = 0;
        //TODO Read ActionTokens from file
    }

    /**
     * Getter
     *
     * @return - returns Lorenzo's current faith
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Getter
     *
     * @return - returns the deck containing all the drawable cards
     */
    public List<ActionToken> getActiveDeck() {
        return activeDeck;
    }

    public void takeTurn() {
        //TODO
    }

    /**
     * Setter for faith attribute
     */
    public void faithIncrease() {
        faith++;
    }

    /**
     * Merges the 2 decks into one and then shuffles it
     */
    public void shuffleDeck() {
        activeDeck.addAll(usedDeck);
        Collections.shuffle(activeDeck);
    }
}
