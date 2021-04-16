package model.card.leadercard;

import model.PlayerBoard;
import model.card.Card;

/**
 * This class represents a generic leader card for the game
 */
public class LeaderCard extends Card {
    //CONSTRUCTORS

    /**
     * Constructor
     * @param victoryPoints the card's victory points
     */
    public LeaderCard(int victoryPoints) {
        super(victoryPoints);
    }

    /**
     * Fast constructor used for testing
     */
    public LeaderCard(){}

    //PUBLIC METHODS

    /**
     * Activates the leader's specific ability
     *
     * @param playerBoard the player for whom to activate the ability
     */
    public void doAction(PlayerBoard playerBoard){}

    /**
     * Returns whether or not the given player meets the requirements for activating the card
     * @param playerBoard the player board to check
     * @return true if the player meets the card's requirements
     */
    public boolean areRequirementsMet(PlayerBoard playerBoard) {return false;}
}
