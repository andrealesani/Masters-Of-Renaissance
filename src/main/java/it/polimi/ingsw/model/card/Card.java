package it.polimi.ingsw.model.card;

import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.model.Color;

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
     * This method is used to print only one line of the Warehouse so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line < 1 || line > 2)
            throw new ParametersNotValidException();

        String content = "";

        switch(line) {

            //Row 1
            case 1 -> content += " Id: " + getId();

            //Row 2
            case 2 -> content += " Victory Points: " + Color.YELLOW_LIGHT_FG + getVictoryPoints() + Color.RESET;
        }

        return content;
    }

    /**
     * Prints a String representation of the card
     *
     * @return the card's String representation
     */
    @Override
    public String toString() {
        String content =    Color.HEADER + "Card:" + Color.RESET +
                            "\n";

        for (int i = 1; i <= 2; i++)
            content +=      printLine(i) +
                            "\n";

        return content;
    }
}
