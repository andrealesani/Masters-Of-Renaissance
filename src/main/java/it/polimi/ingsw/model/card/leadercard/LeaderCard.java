package it.polimi.ingsw.model.card.leadercard;

import it.polimi.ingsw.Exceptions.CardIsActiveException;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.card.Card;

/**
 * This class represents a generic leader card for the game
 */
public class LeaderCard extends Card {

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param victoryPoints the card's victory points
     */
    public LeaderCard(int victoryPoints) {
        super(victoryPoints);
    }

    /**
     * Fast constructor used for testing
     */
    public LeaderCard() {
    }

    //PUBLIC METHODS

    /**
     * Activates the leader's specific ability
     *
     * @param playerBoard the player for whom to activate the ability
     * @throws CardIsActiveException if the card is already active
     */
    public void doAction(PlayerBoard playerBoard) throws CardIsActiveException {
    }

    /**
     * Returns whether or not the given player meets the requirements for activating the card
     *
     * @param playerBoard the player board to check
     * @return true if the player meets the card's requirements
     */
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        return false;
    }
}
