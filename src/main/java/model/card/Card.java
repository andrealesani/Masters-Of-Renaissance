package model.card;

import model.Color;

/**
 * This class represents a generic card in the game. It has methods for activating and deactivating it, and stores its victory points
 */
public abstract class Card {
    /**
     * This attribute stores the card's victory points
     */
    private final int victoryPoints;
    /**
     * This flag determines whether the card is active or not
     */
    private boolean isActive = false;

    private int id = 0;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param victoryPoints the card's victory points
     */
    public Card(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    /**
     * Fast constructor used for testing, sets victory points to zero
     */
    public Card() {
        this.victoryPoints = 0;
    }

    //PUBLIC METHODS

    /**
     * Sets the card 'isActive' attribute to true
     */
    public void activate() {
        isActive = true;
    }

    /**
     * Deactivates the card
     */
    public void deActivate() {
        isActive = false;
    }

    /**
     * Returns whether or not the card is currently active
     *
     * @return true if the card is active
     */
    public boolean isActive() {
        return isActive;
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the card's victory points number
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Getter
     *
     * @return card's id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter
     *
     * @param id that you want to assign to the card
     */
    public void setId(int id) {
        this.id = id;
    }

    //PRINTING METHODS

    /**
     * Returns a String version of the card's information
     *
     * @return the card as a string
     */
    @Override
    public String toString() {
        return  "\n Id: " + getId() +
                "\n Victory Points: " + Color.YELLOW_LIGHT_FG + getVictoryPoints() + Color.RESET;
    }
}
